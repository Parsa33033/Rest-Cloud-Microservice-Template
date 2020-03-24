package com.template.oauth.config;

import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter {

	@Autowired
	RedisTemplate<String, String> redis;
	
	@Override
	public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
		// TODO Auto-generated method stub
		boolean tokenIsBlocked = redis.hasKey(value);
		Jwt jwt = JwtHelper.decode(value);
		String claims = jwt.getClaims();
		JSONObject json = new JSONObject(claims);
		Long exp = ((Integer) json.get("exp")).longValue();
		exp = exp * 1000L + 999;		
		boolean tokenIsExpired = System.currentTimeMillis() > exp;
		if (tokenIsBlocked || tokenIsExpired) {
			return new DefaultOAuth2AccessToken("");
		}
		return super.extractAccessToken(value, map);
	}

}
