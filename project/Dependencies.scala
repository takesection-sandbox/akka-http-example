import sbt._

object Dependencies {

  // AWS
  val AWSSDKVersion = "1.11.125"
  val lambda = "com.amazonaws" % "aws-java-sdk-lambda" % AWSSDKVersion

  // Akka-HTTP
  val akkaHttpVersion = "10.0.5"
  val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
  val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
  val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % "2.4.17"
  val logback = "ch.qos.logback" % "logback-classic" % "1.1.3"
  val akkaHttpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion

  // Specs2
  val specs2 = "org.specs2" %% "specs2-core" % "3.8.9"

  // config
  val config = "com.typesafe" % "config" % "1.3.0"

  // jwt
  val auth0Jwt = "com.auth0" % "java-jwt" % "3.1.0"

  // cors
  val cors = "ch.megard" %% "akka-http-cors" % "0.2.1"

  val commonDeps = Seq(
    specs2 % Test,
    akkaHttpTestkit % Test
  )

  val domainDeps = Seq(
  )

  val rootDeps = Seq(
    config,
    akkaHttp,
    auth0Jwt,
    cors,
    akkaHttpSprayJson,
    akkaSlf4j,
    logback
  )
}