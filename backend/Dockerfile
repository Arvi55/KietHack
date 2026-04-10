# Build Stage
FROM gradle:8.7-jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew build -x test

# Package Stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*SNAPSHOT.jar app.jar

# Render explicitly passes the designated internal PORT as an environment variable natively
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
