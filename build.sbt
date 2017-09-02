import Dependencies._
import cloudformation._
import sbt.Keys._

val Region = "ap-northeast-1"
val BucketName = sys.env.getOrElse("BUCKET_NAME", "YOUR S3 BUCKET NAME")
val KeyName = sys.env.getOrElse("KEY_NAME", "YOUR KEY NAME")
val CertificateArn = sys.env.getOrElse("CERTIFICATE_ARN", "YOUR CERTIFICATE ARN")

lazy val commonSettings = Seq(
  organization := "jp.pigumer",
  scalaVersion := "2.12.3",
  libraryDependencies ++= commonDeps
)

lazy val root = (project in file("./modules/root"))
  .enablePlugins(CloudformationPlugin, JavaAppPackaging, AshScriptPlugin, DockerPlugin)
  .dependsOn(domain)
  .settings(commonSettings: _*)
  .settings(
    name := "akka-http-example",
    dockerBaseImage := "java:8-jdk-alpine",
    dockerExposedPorts := Seq(8080),
    libraryDependencies ++= rootDeps,
    mainClass in assembly := Some("jp.pigumer.boot.Main")
  ).
  settings(
    awscfSettings := AwscfSettings(
      region = Region,
      bucketName = BucketName,
      templates = file("aws/cloudformation")
    ),
    awscfStacks := Map(
      "iam" → CloudformationStack(
        stackName = "iam",
        template = "iam.yaml",
        capabilities = Seq("CAPABILITY_NAMED_IAM")
      ),
      "ecscluster" → CloudformationStack(
        stackName = "ecscluster",
        template = "ecscluster.yaml"
      ),
      "vpc" → CloudformationStack(
        stackName = "vpc",
        template = "vpc.yaml"
      ),
      "subnet" → CloudformationStack(
        stackName = "subnet",
        template = "subnet.yaml"
      ),
      "igw" → CloudformationStack(
        stackName = "igw",
        template = "internetgateway.yaml"
      ),
      "securitygroup" → CloudformationStack(
        stackName = "securitygroup",
        template = "securitygroup.yaml"
      ),
      "alb" → CloudformationStack(
        stackName = "alb",
        template = "alb.yaml",
        parameters = Map("Certificate" → CertificateArn)
      ),
      "ecs" → CloudformationStack(
        stackName = "ecs",
        template = "ecs.yaml",
        parameters = Map(
          "KeyName" → KeyName,
          "ClusterName" → "ECSCluster"
        )
      )
    )
  )

lazy val domain = (project in file("./modules/domain"))
  .settings(commonSettings: _*)
  .settings(
    name := "akka-http-example-domain",
    libraryDependencies ++= domainDeps
  )
