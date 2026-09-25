# Stage 1: Use Maven for Building
FROM maven:3.8.5-openjdk-17-slim as build
WORKDIR /app


COPY ./apigateway ./apigateway

WORKDIR /app/apigateway
RUN mvn clean package -DskipTests

# Stage 2: User OpenJDK for Running
FROM openjdk:27-ea-21-jdk-oracle
WORKDIR /app
COPY --from=build /app/apigateway/target/*.jar apigateway.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "apigateway.jar"]