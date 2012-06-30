package com.updatecontrols.correspondence.binary;

public class HTTPConfiguration {

	private String apiKey;
	private String channelName;
	private String endpoint;
	private int timeoutSeconds;
	
	public HTTPConfiguration(String apiKey, String channelName,
			String endpoint, int timeoutSeconds) {
		super();
		this.apiKey = apiKey;
		this.channelName = channelName;
		this.endpoint = endpoint;
		this.timeoutSeconds = timeoutSeconds;
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getChannelName() {
		return channelName;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public int getTimeoutSeconds() {
		return timeoutSeconds;
	}
}
