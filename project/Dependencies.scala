import sbt._

object Dependencies {

    val akkaHttpVersion = "10.0.5"
    val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion

    val rootDeps = Seq(
        akkaHttp
    )
}