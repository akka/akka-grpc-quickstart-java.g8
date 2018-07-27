package com.example.helloworld;

//#import

import akka.NotUsed;
import akka.japi.Pair;
import akka.stream.Materializer;
import akka.stream.javadsl.BroadcastHub;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.MergeHub;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

//#import

//#service-request-reply
//#service-stream
class GreeterServiceImpl implements GreeterService {

  final Materializer materializer;
  final Sink<HelloRequest, NotUsed> inboundHub;
  final Source<HelloReply, NotUsed> outboundHub;

  public GreeterServiceImpl(Materializer materializer) {
    this.materializer = materializer;

    //#service-request-reply
    Pair<Sink<HelloRequest, NotUsed>, Source<HelloReply, NotUsed>> hubInAndOut =
      MergeHub.of(HelloRequest.class)
        .map(request ->
            HelloReply.newBuilder()
                .setMessage("Hello, " + request.getName())
                .build())
        .toMat(BroadcastHub.of(HelloReply.class), Keep.both())
        .run(materializer);

    inboundHub = hubInAndOut.first();
    outboundHub = hubInAndOut.second();
    //#service-request-reply
  }

  @Override
  public CompletionStage<HelloReply> sayHello(HelloRequest request) {
    return CompletableFuture.completedFuture(
        HelloReply.newBuilder()
            .setMessage("Hello, " + request.getName())
            .build()
    );
  }

  //#service-request-reply
  @Override
  public Source<HelloReply, NotUsed> sayHelloToAll(Source<HelloRequest, NotUsed> in) {
    in.runWith(inboundHub, materializer);
    return outboundHub;
  }
  //#service-request-reply
}
//#service-stream
//#service-request-reply
