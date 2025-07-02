# Quarkus IBM MQ

[![Version](https://img.shields.io/maven-central/v/io.quarkiverse.ibm.mq/quarkus-ibm-mq?logo=apache-maven&style=flat-square)](https://central.sonatype.com/artifact/io.quarkiverse.ibm-mq/quarkus-ibm-mq-parent)

## Installation Steps

You need to build the project locally first to generate the SNAPSHOT and have it install the missing JARs in the local repository

```shell
  mvn clean install -DskipTests
```



## Usage

To use this extension, add the following dependency to your project:

```xml
<dependency>
    <groupId>io.quarkiverse.ibm.mq</groupId>
    <artifactId>quarkus-ibm-mq</artifactId>
    <version>999-SNAPSHOT</version>
</dependency>
```

## Configuration

The following properties can be used to configure the IBM MQ connection:

```properties
quarkus.ironjacamar.ra.kind=ibm-mq
quarkus.ironjacamar.ra.config.host-name=localhost
quarkus.ironjacamar.ra.config.port=1414
quarkus.ironjacamar.ra.config.channel=DEV.ADMIN.SVRCONN
quarkus.ironjacamar.ra.config.queue-manager=QM1
quarkus.ironjacamar.ra.config.username=admin
quarkus.ironjacamar.ra.config.password=admin

quarkus.ironjacamar.activation-spec.foo.config.destination=DEV.QUEUE.1
quarkus.ironjacamar.activation-spec.foo.config.destination-type=jakarta.jms.Queue

```
