# Base image from nodejs
FROM ubuntu:latest

# Define working directory
WORKDIR source

# Update
RUN apt-get update

# Installing softwares
RUN apt-get -y install git openjdk-8-jdk mongodb

# Checkout source code
RUN git clone https://github.com/congngale/web-service-app.git

# Build service
RUN cd web-service-app && ./gradlew bootJar

# Expose web
EXPOSE 80

# Expose TCP Server
EXPOSE 2019

# Build and start
#CMD cd web-service-app && /etc/init.d/mongodb start && ./gradlew bootRun 
#CMD cd web-service-app && ./gradlew bootRun 
CMD java -jar  web-service-app/build/libs/web-service-app-1.0.0.jar
