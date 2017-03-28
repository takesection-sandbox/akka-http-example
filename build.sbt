import Dependencies._

lazy val commonSettings = Seq(
    organization := "jp.pigumer",
    scalaVersion := "2.12.1"
)

lazy val root = (project in file("./modules/root"))
    .settings(commonSettings: _*)
    .settings(
        name := "akka-http-example",
        libraryDependencies ++= rootDeps
    )