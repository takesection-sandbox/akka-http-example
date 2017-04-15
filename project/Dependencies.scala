import sbt._

object Dependencies {

  // Akka-HTTP
  val akkaHttpVersion = "10.0.5"
  val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
  val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion

  // Specs2
  val specs2 = "org.specs2" %% "specs2-core" % "3.8.9"

  // config
  val config = "com.typesafe" % "config" % "1.3.0"

  // jwt
  val auth0Jwt = "com.auth0" % "java-jwt" % "3.1.0"

  // cors
  val cors = "ch.megard" %% "akka-http-cors" % "0.2.1"

  val domainDeps = Seq(
    specs2 % Test,
    config
  )

  val rootDeps = Seq(
    akkaHttp,
    auth0Jwt,
    cors,
    akkaHttpSprayJson
  )
}