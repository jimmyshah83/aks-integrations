FROM eclipse-temurin:17-jdk-jammy
VOLUME /tmp
COPY spring-data-reactive-redis/build/libs/spring-data-reactive-redis-0.0.3-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]