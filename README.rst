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

あらかじめ、キーペアの作成と証明書の発行とその証明書のARNを控えておきます

::

  export BUCKET_NAME=YOUR S3 BUCKET NAME
  export KEY_NAME=YOUR KEY NAME
  export CERTIFICATE_ARN=CERTIFICATE ARN
  sbt

  awscfCreateBucket TemplatesBucket

  awscfUploadTemplates

  awscfCreateStack iam

  awscfCreateStack ecscluster

  awscfCreateStack vpc
  awscfCreateStack subnet
  awscfCreateStack igw
  awscfCreateStack securitygroup

  awscfCreateStack alb

  awscfCreateStack ecs
