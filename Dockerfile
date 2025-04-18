FROM gradle:8.13-jdk21-corretto-al2023 AS build
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build -x test

FROM openjdk:21-jdk-slim
COPY --from=build /home/gradle/build/libs/github-info-0.0.1-SNAPSHOT.jar github-info.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "github-info.jar"]