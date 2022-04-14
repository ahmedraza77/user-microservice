package com.appsdeveloper.app.ws.security;

import com.appsdeveloper.app.ws.AppProperties;
import com.appsdeveloper.app.ws.SpringApplicationContext;

public class SecurityConstants {
	public static final long EXPIRATION_TIME = 864000000;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users";

	public static final String getTokenSecret() {
		AppProperties appProperties =(AppProperties)SpringApplicationContext.getBean("appProperties");
		return appProperties.getTokenSecret();
	}
}