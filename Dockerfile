FROM eclipse-temurin:17-jdk-alpine


RUN apk add --no-cache jq pandoc

WORKDIR /app

COPY target/*.jar app.jar


EXPOSE 8000


CMD ["java", "-jar", "app.jar"]
