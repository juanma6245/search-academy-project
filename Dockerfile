FROM openjdk:17-jdk
VOLUME /tmp
COPY target/*.jar search-0.0.1-SNAPSHOT.jar
EXPOSE 8080 8000
ENTRYPOINT ["java","-jar","/search-0.0.1-SNAPSHOT.jar"]
