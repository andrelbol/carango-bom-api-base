FROM openjdk:11.0.11-jdk-oracle
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-Xmx512m", "-jar", "/app.jar", "--server.port=$PORT"]