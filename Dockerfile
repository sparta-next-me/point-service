FROM bellsoft/liberica-openjdk-alpine:21

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 11112

ENTRYPOINT ["java", "-jar", "app.jar"]
