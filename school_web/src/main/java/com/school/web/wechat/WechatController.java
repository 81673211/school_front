package com.school.web.wechat;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.school.domain.entity.customer.Customer;
import com.school.service.customer.CustomerService;
import com.school.service.wechat.EventService;
import com.school.service.wechat.OauthService;
import com.school.util.wechat.CheckUtil;
import com.school.util.wechat.OAuthToken;
import com.school.util.wechat.UserWechat;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * <b>Description:.</b><br> 
 * @author <b>sil.zhou</b>
 * <br><b>ClassName:</b> 
 * <br><b>Date:</b> 12/06/2018 11:17
 */
@Controller
@RequestMapping("/wx")
@Slf4j
public class WechatController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatController.class);

    @Autowired
    private EventService eventService;
    @Autowired
    private OauthService oauthService;
    @Autowired
    private CustomerService customerService;

    @RequestMapping(method = RequestMethod.POST)
    public void eventHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String result = eventService.process(request);
        LOGGER.info("result:{}", result);

        PrintWriter out = response.getWriter();
        out.println(result);
        out.close();
    }

    @RequestMapping(method = RequestMethod.GET)
    public void checkSignature(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        if (echostr != null && CheckUtil.checkSignature(signature, timestamp, nonce)) {
            PrintWriter out = response.getWriter();
            out.println(echostr);
            out.close();
        }
    }

    @RequestMapping(value = "/proxy", method = RequestMethod.GET)
    public ModelAndView proxy(String code, String state) throws UnsupportedEncodingException {
        ModelAndView mav = new ModelAndView();
        String decodeState = URLDecoder.decode(state, "UTF-8");
        OAuthToken oAuthToken = oauthService.getOAuthToken(code);
        String openId = oAuthToken.getOpenId();
        Customer customer = customerService.getByOpenId(openId);
        if (customer == null) {
            customer = customerService.create(openId);
        }
        if (StringUtils.isBlank(customer.getNickname())) {
            UserWechat userWechat = oauthService.getDetail(openId, oAuthToken.getAccessToken());
            customer.setNickname(userWechat.getNickname());
            customer.setAvatar(userWechat.getAvatar());
            customer.setSex(String.valueOf(userWechat.getSex()));
            customerService.update(customer);
        }
        String viewName;
        if (state.contains("?")) {
            viewName = "redirect:" + decodeState + "&openId=" + openId;
        } else {
            viewName = "redirect:" + decodeState + "?openId=" + openId;
        }
        mav.setViewName(viewName);
        return mav;
    }

}
