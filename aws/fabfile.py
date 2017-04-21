from fabricawscfn import *
import os

bucket_name = os.environ.get('BUCKET_NAME')

StackGroup(
  bucket_name, 'cloudformation', 'cloudformation'
).define_stack(
  'iam', 'iam', 'iam.yaml', Capabilities=['CAPABILITY_NAMED_IAM']
).define_stack(
  'ecscluster', 'ecscluster', 'ecscluster.yaml'
).define_stack(
  'vpc', 'vpc', 'vpc.yaml'
).define_stack(
  'subnet', 'subnet', 'subnet.yaml'
).define_stack(
  'igw', 'igw', 'internetgateway.yaml'
).define_stack(
  'securitygroup', 'securitygroup', 'securitygroup.yaml'
).define_stack(
  'alb', 'alb', 'alb.yaml'
).define_stack(
  'ecs', 'ecs', 'ecs.yaml'
).generate_task(globals())
