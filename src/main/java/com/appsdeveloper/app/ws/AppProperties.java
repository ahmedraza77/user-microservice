package com.appsdeveloper.app.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {
	
	@Autowired
	Environment envProperties;
	
	public String getTokenSecret() {
		return envProperties.getProperty("tokenSecret");
	}

}
