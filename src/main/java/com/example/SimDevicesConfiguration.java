package com.example;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="app")
public class SimDevicesConfiguration {
    public String iotHubConnectionString;
}
