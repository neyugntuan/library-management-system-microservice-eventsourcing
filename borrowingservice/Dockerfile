# Stage 1: Use Maven for Building
FROM maven:3.8.5-openjdk-17-slim as build
WORKDIR /app


COPY ./borrowingservice ./borrowingservice
COPY ./commonservice ./commonservice

WORKDIR /app/commonservice
RUN mvn clean install -DskipTests

WORKDIR /app/borrowingservice
RUN mvn clean package -DskipTests

# Stage 2: User OpenJDK for Running
FROM openjdk:27-ea-21-jdk-oracle
WORKDIR /app
COPY --from=build /app/borrowingservice/target/*.jar borrowingservice.jar
EXPOSE 9004
ENTRYPOINT ["java", "-jar", "borrowingservice.jar"]