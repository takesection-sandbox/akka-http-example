# ecs-cli

[ecs-cli](http://docs.aws.amazon.com/AmazonECS/latest/developerguide/ECS_CLI.html)

# Build Docker image

```
sbt assembly
docker build --build-arg JAR_NAME=akka-http-example.jar .
```
# ECS

```
ecs-cli compose -f docker-compose.yaml --project-name akka-http-example service up
```
