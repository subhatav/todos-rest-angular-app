
# Cache the Dependencies

FROM maven:3.9.2-eclipse-temurin-17-alpine AS builder

WORKDIR /todos-app
COPY pom.xml .

RUN mvn dependency:go-offline

# Package the Application

COPY src/ ./src/
RUN mvn package -DskipTests

# Execute the Application

FROM eclipse-temurin:17-jre-alpine AS runner

LABEL version="1.0.0"
LABEL author="Ph4nToM"
LABEL maintainer="subhatav@gmail.com"

ENV SPRING_PROFILES_ACTIVE=production
COPY --from=builder /todos-app/target/todos-spring-backend-*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
