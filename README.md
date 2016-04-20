# Smart Data Web [![Build Status](https://travis-ci.org/gone-phishing/SDW.svg?branch=master)](https://travis-ci.org/gone-phishing/SDW)
 
Smart Data Web is a BMWi funded project. The central goal of the Smart Data Web project is to leverage state-of-the-art data extraction and enrichment technologies as well as Linked Data to create value-added systems for the German industry. Knowledge which is relevant to decision-making processes will be extracted from government and industry data, official web pages and social media. Then the data will be analyzed using natural language processing frameworks and then it will be integrated into knowledge graphs. These knowledge graphs will be accessible via dashboards and APIs, as well as via Linked Data. Special concern will be given to legal questions, such as data licensing as well as data security and privacy.

**Requirements:**
 - Java 1.8.x
 - Maven 3.3.x
 
**Tools and Dependencies:**
 - Apache Flink
 - Apache Jena
 - Apache Commons-configurations2
 - RabbitMQ
 - Quartz Scheduler
 - RML mapping language
 - R2RML mapping language
 - Docker
 - Sonarqube
 
**Project build:**

`mvn clean install -Pbuild-jar -Dmaven.test.skip=true`

**Running the framework from command line:**

`java -cp ".:target/sdw-1.0.jar:./lib/*" org.sdw.Main`

**Docker build:** 

`sudo docker build -t gonephishing/sdw .` 

**Code Quality:**

Sonarqube has been used for dependency analysis and maintaining the code quality. If the sonarqube server is running during maven build time, the quality report generated can be accessed on [http://localhost:9000](http://localhost:9000)