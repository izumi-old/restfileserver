FROM openjdk:17.0.2-slim
ARG JAR_FILE=build/libs/restfileserver-0.0.1.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
EXPOSE 40045
ENTRYPOINT ["java","-jar","app.jar"]
