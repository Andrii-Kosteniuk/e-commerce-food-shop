FROM eclipse-temurin:17-jre-alpine-3.20
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 9091
ENTRYPOINT ["java", "-jar", "app.jar"]