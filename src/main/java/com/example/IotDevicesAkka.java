package com.example;

import org.springframework.boot.SpringApplication;

public class IotDevicesAkka {

  public static void main(String[] args) {
    SpringApplication.run(SimDevicesApplication.class, args);
//
//    var connectionString = "HostName=vehiothub-s3.azure-devices.net;DeviceId=aklab1;SharedAccessKey=p22gnLQrLAPpoRfjm5p57M7hORzECVKYLZA/lsGxYrY=";
//    //#actor-system
//    final ActorSystem<IotHubDeviceActor.Command> simDeviceMain = ActorSystem.create(IotHubDeviceActor.create(connectionString, "vehicle1"), "vehicle1");
//    //#actor-system
//
//    //#main-send-messages
////    greeterMain.tell(new GreeterMain.SayHello("Charles"));
//    //#main-send-messages
//
//    try {
//      System.out.println(">>> Press ENTER to exit <<<");
//      System.in.read();
//    } catch (IOException ignored) {
//    } finally {
//      simDeviceMain.terminate();
//    }
  }
}
