FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/shop*.jar shop.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=dev", "-jar", "/shop.jar"]