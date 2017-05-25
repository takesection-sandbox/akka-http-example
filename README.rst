Test
====

::

  curl -v -H "Authorization: Bearer 123" http://localhost:8080/?key=abc

Build Docker image
==================

::

  sbt assembly
  docker-compose up -d --build

::

  docker tag {IMAGE_ID} {REPOSITORY_ID}
  docker push {REPOSITORY_ID}

AWS ECS
====

ecs-cli
=======

`ecs-cli <http://docs.aws.amazon.com/AmazonECS/latest/developerguide/ECS_CLI.html>`_

CloudFormation
==============

あらかじめ、キーペアの作成と証明書の発行とその証明書のARNを控えておきます

::

  BUCKET_NAME=YOUR_S3_BUCKET_NAME sbt

::

  awscfCreateBucket TemplatesBucket

  awscfUploadTemplates dev

  awscfCreateStack dev iam

  awscfCreateStack dev ecscluster

  awscfCreateStack dev vpc
  awscfCreateStack dev subnet
  awscfCreateStack dev igw
  awscfCreateStack dev securitygroup

  awscfCreateStack dev alb

  awscfCreateStack dev ecs

::

  awscfListStacks

DNSName、default-tg(target-group-arn)を確認します::

  awscfListExports

::

  ecs-cli compose -f docker-compose.yaml \
    --project-name test-app \
    service up \
    --target-group-arn {default-tg} \
    --container-name akka-http-example \
    --container-port 8080 \
    --role ecsServiceRole

動作確認::

  curl --insecure -H "Authorization: Bearer 123" https://{DNSName}/?key=1234