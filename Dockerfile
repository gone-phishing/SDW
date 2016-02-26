############################################
## Apache Flink 0.10.1 using scala_2.11.4 ##
############################################

FROM ubuntu
MAINTAINER gonephishing <riteshoneinamillion@gmail.com>

RUN apt-get update
RUN apt-get install -y wget
RUN wget https://github.com/apache/flink/archive/release-0.10.1.zip
RUN apt-get install -y unzip
RUN unzip release-0.10.1.zip
WORKDIR /flink-release-0.10.1
RUN tools/change-scala-version.sh 2.11
RUN apt-get install -y maven
RUN apt-get install -y openjdk-7-jdk && rm -rf /var/lib/apt/lists/*
ENV JAVA_HOME /usr/lib/jvm/java-7-openjdk-amd64
RUN ["mvn", "clean", "install", "-DskipTests=true", "-Dmaven.javadoc.skip=true", "-Dscala.version=2.11.4"]
