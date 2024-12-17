FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/fundTransfer-app.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]docker