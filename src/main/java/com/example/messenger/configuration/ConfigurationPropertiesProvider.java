package com.example.messenger.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.environment")
public class ConfigurationPropertiesProvider {

    private String corsHttp;

    ConfigurationPropertiesProvider() {

    }

    public String getCorsHttp() {
        return corsHttp;
    }

    public void setCorsHttp(String corsHttp) {
        this.corsHttp = corsHttp;
    }
}