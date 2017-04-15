Test
====

::

  curl -v -H "Authorization: Bearer 123" http://localhost:8080/?key=abc

ecs-cli
=======

`ecs-cli <http://docs.aws.amazon.com/AmazonECS/latest/developerguide/ECS_CLI.html>`_

Build Docker image
==================

::

  sbt assembly
  docker-compose up -d --build


ECS
====

::

  ecs-cli compose -f docker-compose.yaml --project-name akka-http-example service up

Appendix
========

* `RDS <docs/RDS.md>`_
