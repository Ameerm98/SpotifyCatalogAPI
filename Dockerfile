FROM eclipse-temurin:23-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8082
CMD ["java", "-jar", "app.jar"]