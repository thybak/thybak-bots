FROM eclipse-temurin:17.0.6_10-jdk-focal AS builder
WORKDIR /app
COPY . .
RUN ./gradlew build

FROM eclipse-temurin:17.0.6_10-jre-focal
COPY --from=builder /app/build/libs/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]