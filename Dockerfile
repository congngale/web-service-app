# Base image from nodejs
FROM ubuntu:latest

# Define working directory
WORKDIR source

# Update
RUN apt-get update

# Installing softwares
RUN apt-get -y install git openjdk-8-jdk mongodb

# Start mongodb
RUN /etc/init.d/mongo start

# Checkout source code
RUN git clone https://github.com/congngale/web-service-app.git

# Build service
RUN cd web-service-app && ./gradlew bootJar

# Expose web
EXPOSE 8080

# Expose TCP Server
EXPOSE 2019

# Build and start
CMD ./gradlew bootRun 
