FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/task-management-system-0.0.1-SNAPSHOT.jar /app/task-management-system.jar
ENTRYPOINT ["java", "-jar", "task-management-system.jar"]
