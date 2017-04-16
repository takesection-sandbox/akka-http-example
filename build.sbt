import Dependencies._
import sbt.Keys.mainClass

lazy val commonSettings = Seq(
  version := "$version$",
  organization := "jp.pigumer",
  scalaVersion := "2.12.1"
)

lazy val root = (project in file("./modules/root"))
  .dependsOn(domain)
  .settings(commonSettings: _*)
  .settings(
    name := "akka-http-example",
    libraryDependencies ++= rootDeps,
    mainClass in assembly := Some("jp.pigumer.boot.Main"),
    assemblyJarName in assembly := "akka-http-example.jar"
  )

lazy val domain = (project in file("./modules/domain"))
  .settings(commonSettings: _*)
  .settings(
    name := "akka-http-example-domain",
    libraryDependencies ++= domainDeps
  )
