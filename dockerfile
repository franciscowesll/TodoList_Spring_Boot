From debian:latest As build

RUN sudo apt update
RUN sudo apt install openjdk-17-jdk -y

FROM openjdk: 17-jdk-slim

COPY . .

RUN sudo apt install maven -y
RUN mvn clean install

EXPOSE 8080

COPY --from=build /target/To-do-List-1.0.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
