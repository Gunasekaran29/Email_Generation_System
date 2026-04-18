FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .

RUN mvn clean package -DskipTests -Dmaven.compiler.annotationProcessorPaths=org.projectlombok:lombok

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
COPY --from=build /app/target/mailapp-backend-1.0.0.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]