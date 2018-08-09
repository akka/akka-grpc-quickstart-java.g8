// #full-example
package com.example.helloworld;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class GreeterServiceImplTest {

  private static ActorSystem system;
  private static Materializer materializer;
  private static GreeterService service;

  @BeforeClass
  public static void setup() {
    system = ActorSystem.create();
    materializer = ActorMaterializer.create(system);
    service = new GreeterServiceImpl(materializer);
  }

  @AfterClass
  public static void teardown() {
    TestKit.shutdownActorSystem(system);
    system = null;
    materializer = null;
  }

  @Test
  public void greeterServiceRepliesToSingleRequest() throws Exception {
    HelloReply reply = service.sayHello(HelloRequest.newBuilder().setName("Bob").build())
        .toCompletableFuture()
        .get(5, TimeUnit.SECONDS);
    HelloReply expected = HelloReply.newBuilder().setMessage("Hello, Bob").build();
    assertEquals(expected, reply);
  }

}
// #full-example
