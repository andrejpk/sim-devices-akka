package com.example;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.TimerScheduler;
import com.microsoft.azure.sdk.iot.device.*;
import com.microsoft.azure.sdk.iot.device.exceptions.IotHubClientException;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;

import static akka.pattern.Patterns.pipe;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class IotHubDeviceActor {

    private final TimerScheduler<Command> timers;
    private final String connectionString;
    private final String deviceId;

    private static final Tracer tracer = GlobalOpenTelemetry.getTracer("com.example.SimDevicesAkka");

    interface Command {
    }

    static class Connect implements Command {
    }

    static class SendMessage implements Command {
    }

    private ActorContext<Command> context;

    private IotHubDeviceActor(TimerScheduler<Command> timers, String connectString, String deviceId) {

        this.timers = timers;
        this.connectionString = connectString;
        this.deviceId = deviceId;
        try {
            client = new DeviceClient(connectionString, IotHubClientProtocol.MQTT);
            client.open(false);
        } catch (Exception e) {
            System.out.println("Failed to open IOT Hub client " + e.getMessage());
        }
    }

    public static Behavior<Command> create(String connectionString, String deviceId) {
        return Behaviors.withTimers(timers -> new IotHubDeviceActor(timers, connectionString, deviceId).simDeviceActor());
    }

    private Behavior<Command> simDeviceActor() {
        timers.startTimerAtFixedRate(new SendMessage(), Duration.ofMillis(3000));
        return Behaviors.receive(Command.class)
                .onMessage(SendMessage.class, sendMessage -> onSendMessage(sendMessage))
                .build();
    }

    @WithSpan
    public Behavior<Command> onSendMessage(SendMessage command) {
        Message msg = new Message("{}");
        msg.setContentType("application/json");
        var messageId = UUID.randomUUID().toString();
        msg.setMessageId(messageId);
        var sendFuture = new CompletableFuture();

        var requestSpan = tracer.spanBuilder("IotHub.sendEventAsync").startSpan();
        requestSpan.setAttribute(SemanticAttributes.MESSAGING_SYSTEM, "Azure IoT Hub");
        requestSpan.setAttribute(SemanticAttributes.MESSAGING_MESSAGE_ID, messageId);
        client.sendEventAsync(msg, (message, e, o) -> {
            sendFuture.complete(null);
            requestSpan.end();

            Message returnMsg = (Message) o;
            IotHubStatusCode status = e == null ? IotHubStatusCode.OK : e.getStatusCode();
            System.out.println("Device " + deviceId + "IoT Hub responded to message " + returnMsg.getMessageId() + " with status " + status.name());
        }, msg);

        pipe(sendFuture, null);
        return Behaviors.same();
    }

    private DeviceClient client;

}
