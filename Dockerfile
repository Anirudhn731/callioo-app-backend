FROM maven:3.9.11-amazoncorretto-25 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn -U clean package -DskipTests

FROM amazoncorretto:25

WORKDIR /app

COPY --from=build /app/target/*SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/app/app-0.0.1-SNAPSHOT.jar" ]
