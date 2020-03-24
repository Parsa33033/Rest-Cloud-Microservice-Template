package com.template.oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@ConfigurationProperties("jwt")
@Component
public class JwtProperties {

	private Resource keyStore;
	private String keyStorePassword;
	private String keyAlias;
	private String keyPairPassword;

	public Resource getKeyStore() {
		return keyStore;
	}

	public void setKeyStore(Resource keyStore) {
		this.keyStore = keyStore;
	}

	public String getKeyStorePassword() {
		return keyStorePassword;
	}

	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}

	public String getKeyAlias() {
		return keyAlias;
	}

	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}

	public String getKeyPairPassword() {
		return keyPairPassword;
	}

	public void setKeyPairPassword(String keyPairPassword) {
		this.keyPairPassword = keyPairPassword;
	}

}
