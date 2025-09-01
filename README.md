# daemons

## Getting started

Start localstack:

```bash 
$ docker run --rm \
  --name local-sqs \
  -p 4566:4566 \
  -e SERVICES=sqs \
  -e START_WEB=0 \
  -e LOCALSTACK_HOST=localhost:4566 \
  -d \
  --label quarkus-dev-service-localstack=daemons \
  localstack/localstack:3.7.2
```

Create a new aws cli profile for interacting with localstack:

```bash
$ aws configure --profile localstack
AWS Access Key ID [None]: test-key
AWS Secret Access Key [None]: test-secret
Default region name [None]: eu-west-1
Default output format [None]: text
```

Create a jobs queue:

```bash
$ aws sqs create-queue \
  --queue-name=jobs \
  --profile localstack \
  --endpoint-url=http://localhost:4566
```

Once the app is running, you can send new sqs message using the aws cli:

```bash
$ aws --region eu-west-1 \
  --endpoint-url http://sqs.eu-west-1.localhost:4566 \
  --profile localstack \
  sqs send-message \
  --queue-url "http://sqs.eu-west-1.localhost:4566/000000000000/jobs" \
  --message-body '{"type":"indexAsset","id":["ab952526-3d84-4190-b647-b31a8a64ad50"]}'
```

```bash
$ aws --region eu-west-1 \
  --endpoint-url http://sqs.eu-west-1.localhost:4566 \
  --profile localstack \
  sqs send-message \
  --queue-url "http://sqs.eu-west-1.localhost:4566/000000000000/jobs" \
  --message-body '{"type":"importFTP"}'
  ```

## Quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its
website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode
> only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the
`target/quarkus-app/lib/` directory.

The application is now runnable using
`java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using
`java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build
in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with:
`./target/daemons-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please
consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- Scheduler ([guide](https://quarkus.io/guides/scheduler)): Schedule jobs and
  tasks
- Messaging - Amazon SQS
  Connector ([guide](https://docs.quarkiverse.io/quarkus-amazon-services/dev/amazon-sqs.html)):
  Connect to Amazon SQS with Reactive Messaging
- Amazon
  SQS ([guide](https://docs.quarkiverse.io/quarkus-amazon-services/dev/amazon-sqs.html)):
  Connect to Amazon SQS messaging queue service
