import Dependencies._

lazy val commonSettings = Seq(
    organization := "jp.pigumer",
    scalaVersion := "2.12.1"
)

lazy val root = (project in file("./modules/root"))
    .dependsOn(domain)
    .settings(commonSettings: _*)
    .settings(
        name := "akka-http-example",
        libraryDependencies ++= rootDeps
    )

lazy val domain = (project in file("./modules/domain"))
  .settings(commonSettings: _*)
  .settings(
    name := "akka-http-example-domain",
    libraryDependencies ++= domainDeps
  )
