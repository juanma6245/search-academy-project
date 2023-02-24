FROM openjdk:17-jdk
VOLUME /tmp
COPY target/*.jar search-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/search-0.0.1-SNAPSHOT.jar"]