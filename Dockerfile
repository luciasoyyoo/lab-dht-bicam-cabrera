# Multi-stage Dockerfile for the BiCIAM project
# - Stage "builder" builds the Maven module located in practica5/
# - Stage "runtime" packages a small JRE image with the built JAR

# -----------------
# Builder stage
# -----------------
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy only the module's pom first to leverage Docker layer cache for dependencies
COPY practica5/pom.xml practica5/pom.xml

# Download dependencies (offline) to speed subsequent builds
RUN mvn -B -f practica5/pom.xml -DskipTests dependency:go-offline

# Copy the module source
COPY practica5/ practica5/

# Allow controlling whether tests run during the Docker build
ARG SKIP_TESTS=true

# Build the module. If SKIP_TESTS=true the tests are skipped to speed up image builds.
RUN if [ "$SKIP_TESTS" = "true" ]; then \
			mvn -B -f practica5/pom.xml clean package -DskipTests; \
		else \
			mvn -B -f practica5/pom.xml clean package; \
		fi

# -----------------
# Runtime stage
# -----------------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the produced JAR from the builder stage. Use a wildcard to match the artifact name.
COPY --from=builder /app/practica5/target/*-SNAPSHOT.jar /app/app.jar

# Expose port commonly used by webapps; change if your app uses another port
EXPOSE 8080

# Run the application using the packed JAR on the classpath and explicit main class.
# The project does not produce a "fat" executable JAR with a Main-Class manifest entry,
# so we invoke the known main class directly. Change the main class below if needed.
ENV MAIN_CLASS=es.ull.App
# Use a shell form entrypoint so the environment variable is expanded
# at container runtime. The exec form with a JSON array does not
# perform shell expansion of environment variables.
ENTRYPOINT ["sh", "-c", "exec java -cp /app/app.jar \"$MAIN_CLASS\""]
