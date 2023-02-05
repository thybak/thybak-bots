FROM amazoncorretto:17.0.6-alpine AS builder
WORKDIR /app
COPY . .
RUN ./gradlew build

FROM amazoncorretto:17.0.6-alpine
COPY --from=builder /app/build/libs/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]