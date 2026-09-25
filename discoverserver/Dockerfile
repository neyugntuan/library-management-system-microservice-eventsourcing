# Stage 1: Use Maven for Building
FROM maven:3.8.5-openjdk-17-slim as build
WORKDIR /app
COPY . ./discoverserver
WORKDIR /app/discoverserver
RUN mvn clean package -DskipTests

# Stage 2: User OpenJDK for Running
FROM openjdk:27-ea-21-jdk-oracle
WORKDIR /app
COPY --from=build /app/discoverserver/target/*.jar discoverserver.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "discoverserver.jar"]