FROM java:8
COPY target/ping-generation-system.jar .
ENTRYPOINT ["java","-jar","ping-generation-system.jar"]