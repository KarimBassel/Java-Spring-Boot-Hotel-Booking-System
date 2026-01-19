# ------------------- BUILD STAGE -------------------
FROM eclipse-temurin:21-jdk-jammy AS build

# Set working directory
WORKDIR /app

# Copy Maven wrapper and config first for caching
COPY ./booking/pom.xml .
COPY ./booking/mvnw .
COPY ./booking/.mvn/ .mvn/

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies only
RUN ./mvnw dependency:go-offline -B

# Copy the rest of the source code
COPY ./booking/src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# ------------------- RUNTIME STAGE -------------------
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
