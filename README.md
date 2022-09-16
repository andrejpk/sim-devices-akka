# Sim Devices Akka

## Running

Create a file named `application-local.properties` in the `src/main/resources` folder with the following content:

```properties
app.iot-hub-connection-string=HostName=<yourhubhere>.azure-devices.net;DeviceId=<yourdevicehere>;SharedAccessKey=<YOUROKEYERE>
```

You can get the IOT Hub connection string from the Azure Portal.

Clone the repo and in the root:

```cmd
$ ./mvnw spring-boot:run
```

The app will start one simulated device and start sending messages to IOT Hub.