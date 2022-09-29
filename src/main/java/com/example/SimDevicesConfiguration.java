package com.example;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Configuration
@Validated
@ConfigurationProperties(prefix="app")
public class SimDevicesConfiguration {
    @NotBlank
    public String iotHubConnectionString;
}
