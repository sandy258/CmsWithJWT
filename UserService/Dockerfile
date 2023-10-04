FROM eclipse-temurin:17-jdk-alpine as build

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN sed -i 's/\r$//' mvnw
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM wonderd/jre-alpine as run
COPY --from=build target/user.jar /run/user.jar
EXPOSE 8004
ENTRYPOINT ["java","-jar","/run/user.jar"]