# User used in local builds to access github packages
ARG GITHUB_USER

# Token used in local builds to access github packages
# or GITHUB_TOKEN in CI
ARG GITHUB_TOKEN

# Build stage
FROM maven:3.9.6-amazoncorretto-21-debian AS build
ARG GITHUB_USER
ENV GITHUB_USER=$GITHUB_USER
ARG GITHUB_TOKEN
ENV GITHUB_TOKEN=$GITHUB_TOKEN

# create a new working directory
WORKDIR /app

# copy project files and folders to the current working directory (i.e. 'app' folder)
COPY . .

# installs the maven project from the base PSPWizard
RUN mvn -B install --settings settings.xml --file pom.xml -DskipTests=true -Dmaven.javadoc.skip=true -Dcheckstyle.skipExec=true -Dgithub.token=$GITHUB_TOKEN -Dgithub.name=$GITHUB_USER

# Package stage
FROM eclipse-temurin:21-jdk-alpine AS package

# gets the *.jar file from the api application from the build stage
COPY --from=build /app/target/*.jar app.jar

# exposes the port of the spring app e.g. 8080
EXPOSE 8080

# starts the application
ENTRYPOINT ["java","-jar","app.jar"]