FROM openjdk:11-jre-slim-buster
ADD target/einstufungs-test-0.0.1-SNAPSHOT.jar einstufungs-test-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "einstufungs-test-0.0.1-SNAPSHOT.jar"]

