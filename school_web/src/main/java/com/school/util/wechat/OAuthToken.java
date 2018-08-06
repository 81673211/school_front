package com.school.util.wechat;

import lombok.Data;

/**
 * 网页授权接口调用凭证 OAuth2.0
 * @author caspar.chen
 * @version 1.0
 */
@Data
public class OAuthToken {
	/**
	 * 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	 */
	private String accessToken;

	/**
	 * access_token接口调用凭证超时时间，单位（秒）
	 */
	private int expiresIn;

	/**
	 * 用户刷新access_token
	 */
	private String refreshToken;

	/**
	 * 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
	 */
	private String openId;

	/**
	 * 用户授权的作用域，使用逗号（,）分隔
	 */
	private String scope;

	/**
	 * 缓存时间.
	 */
	private long cacheTime;

	public OAuthToken() {
	}

	public OAuthToken(String accessToken, int expiresIn, String refreshToken, String openId, String scope,
					  long cacheTime) {
		this.accessToken = accessToken;
		this.expiresIn = expiresIn;
		this.refreshToken = refreshToken;
		this.openId = openId;
		this.scope = scope;
		this.cacheTime = cacheTime;
	}
}
