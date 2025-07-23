# Use Eclipse Temurin (OpenJDK 17) as base image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the entire project
COPY . .

# Give execute permission to Maven wrapper
RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# Run the JAR file
CMD ["sh", "-c", "java -jar target/*.jar"]
