package com.template.oauth.auth;

public class AddResourceToClientResponse {

	private String clientId;
	private boolean resourceAdded;
	private String message;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public boolean isResourceAdded() {
		return resourceAdded;
	}

	public void setResourceAdded(boolean resourceAdded) {
		this.resourceAdded = resourceAdded;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
