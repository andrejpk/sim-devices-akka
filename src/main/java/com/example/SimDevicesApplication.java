package com.example;

import akka.actor.typed.ActorSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
class SimDevicesApplication {
    final static Logger logger = LoggerFactory.getLogger(SimDevicesApplication.class);

    @Bean
    ActorSystem actorSystem(SimDevicesConfiguration config) {

        var system = ActorSystem.create(IotHubDeviceActor.create(
                config.iotHubConnectionString,
                "vehicle1")
            , "actor-system");
        return system;
    }
}
