############################################
## Apache Flink 1.0.0 using scala_2.11.4 ##
############################################

# Set base image as ubuntu
FROM ubuntu:14.04
MAINTAINER gonephishing <riteshoneinamillion@gmail.com>

# Update and install necessary tools
RUN apt-get update -y
RUN apt-get install -y software-properties-common
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
RUN add-apt-repository -y ppa:webupd8team/java
RUN apt-get update
RUN apt-get install -y wget 
RUN apt-get install -y unzip 
RUN apt-get install -y oracle-java8-installer && rm -rf /var/lib/apt/lists/* && rm -rf /var/cache/oracle-jdk8-installer
RUN apt-get install -y maven

# Get the flink release from github and build it on the image
RUN wget https://github.com/apache/flink/archive/release-1.0.0.zip
RUN unzip release-0.10.1.zip
WORKDIR /flink-release-0.10.1
RUN tools/change-scala-version.sh 2.11
RUN ["mvn", "clean", "install", "-DskipTests=true", "-Dmaven.javadoc.skip=true", "-Dscala.version=2.11.4"]

# Get the Smart data web framework from github and build it using maven
RUN wget https://github.com/gone-phishing/SDW/archive/master.zip
RUN unzip master.zip
WORKDIR SDW-master/
RUN ["mvn", "clean", "package", "-Pbuild-jar", "-Dmaven.test.skip=true"]