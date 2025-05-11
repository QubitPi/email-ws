Email Webservice
================

__Email Webservice__ is a Spring Boot application template that lets us set up email delivery web service with minimal
effort.

Documentation
-------------

```console
git clone git@github.com:QubitPi/email-ws.git
cd email-ws
mvn clean package
java -jar target/email-ws-0.0.1-SNAPSHOT.jar
```

- Healthcheck: http://localhost:8080/actuator/health

Development
-----------

- Starting locally:

  ```console
  mvn clean package
  java -jar target/email-ws-0.0.1-SNAPSHOT.jar
  ```

- Running tests:

  ```console
  mvn clean verify
  ```
