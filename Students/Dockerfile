FROM openjdk:17-oracle
WORKDIR /app
COPY build/libs/studentRecords-0.0.1-SNAPSHOT.jar app.jar
ENV ADD_ON_START=true
CMD ["java","-jar","app.jar"]