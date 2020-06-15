// #full-example
package com.example.helloworld;

import akka.actor.testkit.typed.javadsl.ActorTestKit;
import akka.actor.typed.ActorSystem;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class GreeterServiceImplTest {

  private static final ActorTestKit testKit = ActorTestKit.create();

  private static ActorSystem<?> system = testKit.system();
  private static GreeterService service;

  @BeforeClass
  public static void setup() {
    service = new GreeterServiceImpl(system);
  }

  @AfterClass
  public static void teardown() {
    testKit.shutdownTestKit();
    system = null;
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
