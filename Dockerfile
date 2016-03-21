############################################
## Apache Flink 1.0.0 using scala_2.11.4 ##
############################################

# Set base image as ubuntu
FROM ubuntu:14.04
MAINTAINER gonephishing <riteshoneinamillion@gmail.com>

# Update and install necessary tools
RUN apt-get update
RUN apt-get install -y wget 
RUN apt-get install -y unzip 
RUN apt-get install -y maven
RUN apt-get install -y openjdk-7-jdk && rm -rf /var/lib/apt/lists/*
ENV JAVA_HOME /usr/lib/jvm/java-7-openjdk-amd64

# Get the flink release from github and build it on the image
RUN wget https://github.com/apache/flink/archive/release-1.0.0.zip
RUN unzip release-0.10.1.zip
WORKDIR /flink-release-0.10.1
RUN tools/change-scala-version.sh 2.11
RUN ["mvn", "clean", "install", "-DskipTests=true", "-Dmaven.javadoc.skip=true", "-Dscala.version=2.11.4"]
