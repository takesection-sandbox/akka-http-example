import Dependencies._
import sbt.Keys._
import jp.pigumer.sbt.cloud.aws.cloudformation._

val Region = "ap-northeast-1"
val BucketName = sys.env.getOrElse("BUCKET_NAME", "YOUR S3 BUCKET NAME")

lazy val commonSettings = Seq(
  organization := "jp.pigumer",
  scalaVersion := "2.12.6",
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
      bucketName = Some(BucketName),
      templates = Some(file("aws/cloudformation"))
    ),
    awscfStacks := Stacks(
      Alias("iam") → CloudformationStack(
        stackName = "iam",
        template = "iam.yaml",
        capabilities = Seq("CAPABILITY_NAMED_IAM")
      ),
      Alias("ecscluster") → CloudformationStack(
        stackName = "ecscluster",
        template = "ecscluster.yaml"
      ),
      Alias("vpc") → CloudformationStack(
        stackName = "vpc",
        template = "vpc.yaml"
      ),
      Alias("subnet") → CloudformationStack(
        stackName = "subnet",
        template = "subnet.yaml"
      ),
      Alias("igw") → CloudformationStack(
        stackName = "igw",
        template = "internetgateway.yaml"
      ),
      Alias("securitygroup") → CloudformationStack(
        stackName = "securitygroup",
        template = "securitygroup.yaml"
      ),
      Alias("alb") → CloudformationStack(
        stackName = "alb",
        template = "alb.yaml"
      ),
      Alias("ecs") → CloudformationStack(
        stackName = "ecs",
        template = "ecs.yaml",
        parameters = Map(
          "ImageId" → "ami-b743bed1",
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
