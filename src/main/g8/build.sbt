name := "$name$"
version := "1.0"
scalaVersion := "$scala_version$"
val akkaVersion = "$akka_version$"

enablePlugins(AkkaGrpcPlugin)

akkaGrpcGeneratedLanguages := Seq(AkkaGrpc.Java)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-discovery" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
  "junit" % "junit" % "4.13" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")

// TODO remove when Akka gRPC 1.0.0 is final
resolvers += Resolver.bintrayRepo("akka", "snapshots")
