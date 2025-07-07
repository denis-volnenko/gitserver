FROM eclipse-temurin:17-jdk
MAINTAINER denis@volnenko.ru

COPY ./target/gitserver/ /opt/
WORKDIR /opt/bin

RUN mkdir -p /opt/data

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "gitserver.jar"]