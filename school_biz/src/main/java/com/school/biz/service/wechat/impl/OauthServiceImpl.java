package com.school.biz.service.wechat.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.school.biz.constant.ConfigProperties;
import com.school.biz.constant.Constants;
import com.school.biz.constant.RedisKeyNS;
import com.school.biz.constant.WechatUrl;
import com.school.biz.domain.bo.wechat.OAuthToken;
import com.school.biz.domain.bo.wechat.UserWechat;
import com.school.biz.enumeration.AuthResultEnum;
import com.school.biz.service.wechat.OauthService;
import com.school.biz.util.HttpUtil;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * <b>Description:.</b><br> 
 * @author <b>sil.zhou</b>
 * <br><b>ClassName:</b> 
 * <br><b>Date:</b> 12/06/2018 21:24
 */
@Service
@Slf4j
public class OauthServiceImpl implements OauthService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public UserWechat getDetail(String openId, String accessToken) {
        String userInfoUrl = WechatUrl.USER_INFO_URL.replace("${ACCESS_TOKEN}", accessToken).replace(
                "${OPEN_ID}", openId);
        String response;
        try {
            response = HttpUtil.get(userInfoUrl, Constants.CHARSET_UTF8, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject json = JSON.parseObject(response);
        return new UserWechat(json.getString("openid"),
                              json.getString("nickname"),
                              json.getIntValue("sex"),
                              json.getString("headimgurl"));
    }

    @Override
    public String getOAuthUrl(String state) {
        try {
            String url = WechatUrl.USER_AUTH_URL
                    .replace("${APPID}", ConfigProperties.APPID)
                    .replace("${REDIRECT_URL}",
                             URLEncoder.encode("http://www.glove1573.cn/wx/proxy", Constants.CHARSET_UTF8))
                    .replace("${SCOPE}", Constants.SCOPE_SNSAPI_USERINFO)
                    .replace("${STATE}", URLEncoder.encode(state, Constants.CHARSET_UTF8));
            return url;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OAuthToken getOAuthToken(String code) {

        String getOAuthTokenUrl = WechatUrl.OAUTH_TOKEN_GET_URL
                .replace("${APPID}", ConfigProperties.APPID)
                .replace("${APPSECRET}", ConfigProperties.APPSECRET)
                .replace("${CODE}", code);
        String response;
        try {
            response = HttpUtil.get(getOAuthTokenUrl, Constants.CHARSET_UTF8, false);
            log.info("getAuthToken, code:{}, response:{}", code, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject json = JSON.parseObject(response);
        OAuthToken authToken = new OAuthToken(json.getString("access_token"),
                                              json.getIntValue("expires_in"),
                                              json.getString("refresh_token"),
                                              json.getString("openid"),
                                              json.getString("scope"),
                                              System.currentTimeMillis());
        String toJSONString = JSON.toJSONString(authToken);
        redisTemplate.opsForValue().set(RedisKeyNS.CACHE_NAMESPACE_AUTH_TOKEN + authToken.getOpenId(),
                                        toJSONString);
        return authToken;
    }

    @Override
    public int check(String openId) {
        String accessToken = redisTemplate.opsForValue().get(RedisKeyNS.CACHE_NAMESPACE_AUTH_TOKEN + openId);
        OAuthToken authToken = JSON.parseObject(accessToken, OAuthToken.class);
        if (authToken == null) {
            log.warn("有未授权访问网页发生，openId:{}", openId);
            return AuthResultEnum.FAIL.getCode();
        }
        if (hasExpired(authToken)) {
            log.info("get refreshToken");
            String refreshTokenUrl = WechatUrl.OAUTH_TOKEN_REFRESH_URL
                    .replace("${APPID}", ConfigProperties.APPID)
                    .replace("${REFRESH_TOKEN}", authToken.getRefreshToken());
            String response;
            try {
                response = HttpUtil.get(refreshTokenUrl, Constants.CHARSET_UTF8, false);
                log.info("getRefreshAuthToken response:{}", response);
            } catch (IOException e) {
                log.error(e.getMessage());
                return AuthResultEnum.FAIL.getCode();
            }
            JSONObject json = JSON.parseObject(response);
            if (json.containsKey("errcode")) {
                if (json.getIntValue("errcode") == 42002) {
                    log.info("refreshToken超时，重新授权， openId:{}", openId);
                    return AuthResultEnum.EXPIRE.getCode();
                } else {
                    log.error(json.getString("errmsg"));
                    return AuthResultEnum.FAIL.getCode();
                }
            }
            authToken = new OAuthToken(json.getString("access_token"),
                                       json.getIntValue("expires_in"),
                                       json.getString("refresh_token"),
                                       json.getString("openid"),
                                       json.getString("scope"),
                                       System.currentTimeMillis());
            redisTemplate.opsForValue().set(RedisKeyNS.CACHE_NAMESPACE_AUTH_TOKEN + authToken.getOpenId(),
                                            JSON.toJSONString(authToken));
            return AuthResultEnum.SUCCESS.getCode();
        } else {
            return AuthResultEnum.SUCCESS.getCode();
        }
    }

    private boolean hasExpired(OAuthToken token) {
        return (System.currentTimeMillis() - token.getCacheTime()) > token.getExpiresIn() * 1000;
    }
}
