# Spring Boot Build and Run

    mvn clean package -Pdev -f pom.xml

    mvn spring-boot:run

# Spring Boot Build and Run with Docker

    docker build -t shop .

    docker run -it -p 8080:8080 shop

# Authentication

### ADMIN
    username: admin
    password: password
    roles: USER, ADMIN
    permission: GET, POST, DELETE

### USER
    username: user
    password: password
    roles: USER
    permission: GET

# Messaging system is activeMQ and use jms

### Configuration in application.properties
    jms.broker-url = URL
    jms.msg.consumer.destination.shop = shop
    jms.msg.consumer.concurrency = 5

### Required
    How to Install Apache ActiveMQ on Ubuntu 18.04 | 16.04
    From <https://websiteforstudents.com/how-to-install-apache-activemq-on-ubuntu-18-04-16-04/>****

# API Test

    Postman - src/test/resources/collections