#FROM jeanblanchard/java:jre-8
FROM openjdk:8-jre-alpine
MAINTAINER Philipp Waibel

ADD target/viepep-c-backendservice-0.0.1-SNAPSHOT.jar /target.jar

RUN touch /tmp/input.tmp

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/target.jar", "-Djava.io.tmpdir=/tmp"]
