# Use official JDK image
FROM eclipse-temurin:17-jdk-alpine AS build

# Set working directory
WORKDIR /app

# Copy the source code
COPY . .

RUN chmod +x mvnw

# Run Maven to build the project and create the jar
RUN ./mvnw clean package -DskipTests

# === Production Image ===
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /app/target/MoneyMavenTonita-0.0.1-SNAPSHOT.jar app.jar

# Expose the port (required by Render)
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "app.jar"]
