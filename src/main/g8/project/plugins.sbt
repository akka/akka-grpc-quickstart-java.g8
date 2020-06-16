// addSbtPlugin("com.lightbend.akka.grpc" % "sbt-akka-grpc" % "$akka_grpc_version$")
addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")

// TODO remove when Akka gRPC 1.0.0 is final
addSbtPlugin("com.lightbend.akka.grpc" % "sbt-akka-grpc" % "1.0.0-M1+35-c8aa943b")
resolvers += Resolver.bintrayRepo("akka", "snapshots")
