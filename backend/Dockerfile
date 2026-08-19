# Step 1: build the spring boot JAR with java 21 and maven
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build 
WORKDIR /app
COPY . . 
RUN chmod +x ./mvnw || true
RUN mvn clean package -DskipTests   

# Step 2: Lightweight Production Runtime                                             
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app 
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080 
ENTRYPOINT ["java", "-jar", "app.jar"]