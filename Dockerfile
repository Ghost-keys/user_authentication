# Stage 1: Resolve and download dependencies
FROM eclipse-temurin:21-jdk-jammy AS deps

WORKDIR /build

# Copy the Maven wrapper with executable permissions and the Maven settings
COPY --chmod=0755 mvnw mvnw
COPY .mvn/ .mvn/
COPY pom.xml pom.xml

# Download dependencies as a separate step to take advantage of Docker's caching
RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline -DskipTests

################################################################################

# Stage 2: Build the application based on the stage with downloaded dependencies
FROM deps AS package

WORKDIR /build

# Copy the application source code and build the application
COPY src src
RUN --mount=type=cache,target=/root/.m2 ./mvnw package -DskipTests && \
    mv target/$(./mvnw help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar

################################################################################

# Stage 3: Extract the application into separate layers
FROM package AS extract

WORKDIR /build

RUN java -Djarmode=layertools -jar target/app.jar extract --destination target/extracted

################################################################################

# Stage 4: Create the final minimal runtime image
FROM eclipse-temurin:21-jre-jammy AS final

# Create a non-privileged user to run the application
ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser

# Set the working directory and copy the extracted layers from the extract stage
WORKDIR /app
COPY --from=extract /build/target/extracted/dependencies/ ./
COPY --from=extract /build/target/extracted/spring-boot-loader/ ./
COPY --from=extract /build/target/extracted/snapshot-dependencies/ ./
COPY --from=extract /build/target/extracted/application/ ./

# Copy the entrypoint script
COPY docker_setup_env.sh .
RUN chmod +x docker_setup_env.sh

# Change ownership of the application files to the non-root user
RUN chown -R appuser:appuser /app

# Switch to the non-privileged user
USER appuser

# Expose the application port
EXPOSE 1010

# Set the entrypoint to launch the application
ENTRYPOINT ["./docker_setup_env.sh"]
