# Pull a lightweight version of Ubuntu
FROM ubuntu:22.04


# Set environment variables for non-interactive installations
ENV DEBIAN_FRONTEND=noninteractive

# Install Java 17, Maven, Git, and other necessary packages
RUN apt-get update && \
    apt-get install -y --no-install-recommends openjdk-17-jdk \
    maven -y \
    git -y \
    ca-certificates \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Set JAVA_HOME environment variable
RUN export JAVA_HOME="$(dirname $(dirname $(readlink -f $(which java))))"
RUN export MAVEN_HOME="$(dirname $(dirname $(readlink -f $(which mvn))))"
ENV PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH




# Verify installations
RUN echo $PATH

RUN echo $JAVA_HOME && \
    echo "Print java Path" && \
    which java && \
    java -version && \
    javac -version && \
    echo "Print MVN path" && \
    which mvn && \
    which git && \
    mvn -version && \
    git --version


# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the user service project files into the container
COPY . /usr/src/app

# Build the project using Maven
RUN mvn clean package -Dmaven.test.skip=true

# Expose the port that the user service will run on
EXPOSE 8081

# Run the user service
CMD ["java", "-jar", "target/UserService-0.0.1-SNAPSHOT.jar"]