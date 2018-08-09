// #full-example
package com.example.helloworld;

import akka.actor.ActorSystem;
import akka.grpc.GrpcClientSettings;
import akka.http.javadsl.ServerBinding;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.testkit.javadsl.TestKit;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class GreeterTest {

  private static ActorSystem serverSystem;
  private static ActorSystem clientSystem;
  private static GreeterServiceClient client;
  
  @BeforeClass
  public static void setup() throws Exception {
    // important to enable HTTP/2 in server ActorSystem's config
    Config config = ConfigFactory.parseString("akka.http.server.preview.enable-http2 = on")
        .withFallback(ConfigFactory.defaultApplication());
    serverSystem = ActorSystem.create("HelloWorldServer", config);
    CompletionStage<ServerBinding> bound = new GreeterServer(serverSystem).run();
    // make sure server is bound before using client
    bound.toCompletableFuture().get(5, TimeUnit.SECONDS);

    clientSystem = ActorSystem.create("HelloWorldClient");
    Materializer clientMaterializer = ActorMaterializer.create(clientSystem);
    // the host and TLS certificate config are picked up from the config file
    client = GreeterServiceClient.create(
        GrpcClientSettings.fromConfig("helloworld.GreeterService", clientSystem),
        clientMaterializer,
        clientSystem.dispatcher()
      );

  }

  @AfterClass
  public static void teardown() {
    TestKit.shutdownActorSystem(serverSystem);
    TestKit.shutdownActorSystem(clientSystem);
    serverSystem = null;
    client = null;
  }

  @Test
  public void greeterServiceRepliesToSingleRequest() throws Exception {
    HelloReply reply = client.sayHello(HelloRequest.newBuilder().setName("Alice").build())
        .toCompletableFuture()
        .get(5, TimeUnit.SECONDS);
    HelloReply expected = HelloReply.newBuilder().setMessage("Hello, Alice").build();
    assertEquals(expected, reply);
  }

}
// #full-example
