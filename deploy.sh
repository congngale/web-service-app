#!/bin/sh

# Stop docker container if running
echo "Stoping backend application"
docker stop back-end

# Remove running container
echo "Removing backend container"
docker rm back-end

# Remove flight reservarion local image
echo "Removing flight reservation local image"
docker rmi flight-reservation

# Remove flight reservation heroku image
echo "Removing flight reservation heroku image"
docker rmi registry.heroku.com/flight-reservation/web

# Build docker image for flight reservation back end
echo "Start to build docker images for flight reservation backend"
docker build -t flight-reservation .

# Run flight reservation backend
echo "Start to run flight reservation backend"
docker run -d -p 80:3000 --name back-end flight-reservation

# Login into heroku
echo "Login into heroku server"
heroku login

# Login into heroku container
echo "Login into heroku container"
heroku container:login

# Deploy flight reservation application into heroku
echo "Start to deploy flight reservation backend"
heroku container:push web --app flight-reservation

# Start flight reservation backend
echo "Start flight reservation backend"
heroku open --app flight-reservation
