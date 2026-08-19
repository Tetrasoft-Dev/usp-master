FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN --mount=type=secret,id=maven_username,env=MAVEN_USERNAME \
    --mount=type=secret,id=maven_token,env=MAVEN_TOKEN \
    mkdir -p /root/.m2 && \
    printf '%s\n' \
      '<?xml version="1.0" encoding="UTF-8"?>' \
      '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"' \
      '          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"' \
      '          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0' \
      '          https://maven.apache.org/xsd/settings-1.0.0.xsd">' \
      '  <servers>' \
      '    <server>' \
      '      <id>github</id>' \
      "      <username>${MAVEN_USERNAME}</username>" \
      "      <password>${MAVEN_TOKEN}</password>" \
      '    </server>' \
      '  </servers>' \
      '</settings>' \
      > /root/.m2/settings.xml && \
    mvn clean package -DskipTests && \
    rm -f /root/.m2/settings.xml


FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

RUN mkdir -p /app/uploads

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]