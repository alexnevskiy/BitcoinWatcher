FROM maven:3.6.0-jdk-11-slim AS build
COPY src /src
COPY pom.xml .
RUN mvn clean package

FROM openjdk:11-jre-slim
COPY --from=build /target/BitcoinWatcher-0.0.1.jar BitcoinWatcher-0.0.1.jar
ENTRYPOINT ["java","-jar","BitcoinWatcher-0.0.1.jar"]