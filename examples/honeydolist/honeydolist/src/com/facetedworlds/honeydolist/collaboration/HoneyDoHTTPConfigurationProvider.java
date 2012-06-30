package com.facetedworlds.honeydolist.collaboration;

import com.updatecontrols.correspondence.binary.HTTPConfiguration;
import com.updatecontrols.correspondence.binary.HTTPConfigurationProvider;

public class HoneyDoHTTPConfigurationProvider implements HTTPConfigurationProvider {

	public HTTPConfiguration getConfiguration() {
		String apiKey = "9FE2E1C9102D4C11AC6D7E8272001A0F";
		String channelName = "HoneyDoList";
		String endpoint = "https://api.facetedworlds.com/correspondence_server_web/bin";
		int timeoutSeconds = 0;
		
		return new HTTPConfiguration(apiKey , channelName, endpoint, timeoutSeconds);
	}
}
