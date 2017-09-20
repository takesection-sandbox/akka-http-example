Test
====

::



Build Docker image
==================

::

  $ sbt

  > docker:stage
  > docker:publishLocal
  > exit

  $ docker run -d -p 8080:8080 --rm <image name>:<tag>
  $ curl -v -H "Authorization: Bearer 123" http://localhost:8080/?key=abc

CloudFormation
==============

::

  BUCKET_NAME=<YOUR BUCKET NAME> sbt

  > awscfCreateBucket TemplatesBucket

  > awscfUploadTemplates

  > awscfCreateStack iam

  > awscfCreateStack ecscluster

  > awscfCreateStack vpc
  > awscfCreateStack subnet
  > awscfCreateStack igw
  > awscfCreateStack securitygroup

  > awscfCreateStack alb

  > awscfCreateStack ecs
