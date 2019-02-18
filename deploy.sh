#!/bin/sh

echo "Login in to heroku"
heroku login

echo "Login in to container"
heroku container:login

echo "Push docker image"
heroku container:push web --app network-web-service

echo "Deploy web-service-app"
heroku container:release web --app network-web-service
