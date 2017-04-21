Test
====

::

  curl -v -H "Authorization: Bearer 123" http://localhost:8080/?key=abc

Build Docker image
==================

::

  sbt assembly
  docker-compose up -d --build

AWS ECS
====

ecs-cli
=======

`ecs-cli <http://docs.aws.amazon.com/AmazonECS/latest/developerguide/ECS_CLI.html>`_

fabric
======

::

  pip install fabric
  pip install git+https://github.com/crossroad0201/fabric-aws-cloudformation.git

CloudFormation
==============

あらかじめ、キーペアの作成と証明書の発行とその証明書のARNを控えておきます

::

  cd aws
  BUCKET_NAME=YOUR_S3_BUCKET_NAME fab list_stacks

::

  BUCKET_NAME=YOUR_S3_BUCKET_NAME fab sync_templates

  BUCKET_NAME=YOUR_S3_BUCKET_NAME fab create_iam

  BUCKET_NAME=YOUR_S3_BUCKET_NAME fab create_ecscluster

  BUCKET_NAME=YOUR_S3_BUCKET_NAME fab create_vpc
  BUCKET_NAME=YOUR_S3_BUCKET_NAME fab create_subnet
  BUCKET_NAME=YOUR_S3_BUCKET_NAME fab create_igw
  BUCKET_NAME=YOUR_S3_BUCKET_NAME fab create_securitygroup

  BUCKET_NAME=YOUR_S3_BUCKET_NAME fab create_alb

  BUCKET_NAME=YOUR_S3_BUCKET_NAME fab create_ecs

::

  BUCKET_NAME=YOUR_S3_BUCKET_NAME fab list_stacks

DNSName、default-tg(target-group-arn)を確認します::

  BUCKET_NAME=YOUR_S3_BUCKET_NAME fab list_exports

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