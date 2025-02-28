# Quarkus IBM MQ

[![Version](https://img.shields.io/maven-central/v/io.quarkiverse.ibm.mq/quarkus-ibm-mq?logo=apache-maven&style=flat-square)](https://central.sonatype.com/artifact/io.quarkiverse.ibm-mq/quarkus-ibm-mq-parent)

## Installation Steps

Because the `com.ibm.mq.jakarta.connector-9.4.2.0.jar` isn't available in Maven, first thing you need to do is to install
it in your local Maven repository:

### Download the RAR file

Download the RAR file
from https://repo1.maven.org/maven2/com/ibm/mq/wmq.jakarta.jmsra/9.4.2.0/wmq.jakarta.jmsra-9.4.2.0.rar and extract the
`com.ibm.mq.jakarta.connector.jar` file inside of it.

### Install the Connector JAR in your local Maven repository

```shell 
  mvn install:install-file -Dfile=com.ibm.mq.jakarta.connector.jar  -DgroupId=com.ibm.mq -DartifactId=com.ibm.mq.jakarta.connector -Dversion=9.4.2.0 -Dpackaging=jar
```

Now you can build this project with the following command:

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
quarkus.ironjacamar.ra.config.host=localhost
quarkus.ironjacamar.ra.config.port=1414
quarkus.ironjacamar.ra.config.channel=DEV.ADMIN.SVRCONN
quarkus.ironjacamar.ra.config.queue-manager=QM1
```
