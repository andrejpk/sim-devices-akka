package com.example;

import akka.actor.typed.ActorSystem;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
class SimDevicesApplication {
    final static Logger logger = LoggerFactory.getLogger(SimDevicesApplication.class);

    @Bean
    ActorSystem actorSystem(SimDevicesConfiguration config) {

        logger.info("Staring using x " + config.x);
        var system = ActorSystem.create(IotHubDeviceActor.create(
                config.iotHubConnectionString,
                "vehicle1")
            , "actor-system");
        return system;
    }
}
