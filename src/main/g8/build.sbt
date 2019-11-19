name := "$name$"
version := "1.0"
scalaVersion := "$scala_version$"
val akkaVersion = "$akka_version$"

enablePlugins(AkkaGrpcPlugin)

akkaGrpcGeneratedLanguages := Seq(AkkaGrpc.Java)

// ALPN agent
enablePlugins(JavaAgent)
javaAgents += "org.mortbay.jetty.alpn" % "jetty-alpn-agent" % "$jetty_alpn_agent_version$" % "runtime;test"


libraryDependencies ++= Seq(
  // The explicit ssl-config dependency can be removed when Akka 2.6.1
  // has been released
  "com.typesafe" %% "ssl-config-core" % "0.4.1",
  "com.typesafe.akka" %% "akka-discovery" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
  "junit" % "junit" % "4.12" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")
