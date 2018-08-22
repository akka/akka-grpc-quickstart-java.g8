package com.example.helloworld;

//#import
import akka.actor.ActorSystem;
import akka.http.javadsl.*;
import akka.http.javadsl.model.*;
import akka.http.javadsl.settings.ServerSettings;
import akka.japi.Function;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.util.concurrent.CompletionStage;
//#import

//#server
class GreeterServer {

  static void main(String[] args) throws Exception {
    // important to enable HTTP/2 in ActorSystem's config
    Config conf = ConfigFactory.parseString("akka.http.server.preview.enable-http2 = on")
      .withFallback(ConfigFactory.defaultApplication());
    ActorSystem system = ActorSystem.create("HelloWorld", conf);
    new GreeterServer(system).run();
  }

  final ActorSystem system;

  public GreeterServer(ActorSystem system) {
    this.system = system;
  }

  public CompletionStage<ServerBinding> run() throws Exception {

    Materializer materializer = ActorMaterializer.create(system);

    Function<HttpRequest, CompletionStage<HttpResponse>> service =
        GreeterServiceHandlerFactory.create(
            new GreeterServiceImpl(materializer),
            materializer);

    CompletionStage<ServerBinding> bound =
        Http.get(system).bindAndHandleAsync(
          service,
          ConnectHttp.toHost("127.0.0.1", 8080, UseHttp2.always()),
          ServerSettings.create(system),
          // Needed due to https://github.com/akka/akka-http/issues/2168
          256,
          system.log(),
          materializer
        );

    bound.thenAccept(binding ->
        System.out.println("gRPC server bound to: " + binding.localAddress())
    );

    return bound;
  }

}
//#server