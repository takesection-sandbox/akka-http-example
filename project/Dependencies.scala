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

  val domainDeps = Seq(
    specs2 % Test,
    config
  )

  val rootDeps = Seq(
    akkaHttp,
    akkaHttpSprayJson
  )
}