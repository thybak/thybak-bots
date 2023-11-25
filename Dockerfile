FROM gradle:jdk17-focal AS builder
COPY --chown=gradle:gradle .  /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

FROM eclipse-temurin:17.0.6_10-jre-focal
COPY --from=builder /home/gradle/src/build/libs/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]