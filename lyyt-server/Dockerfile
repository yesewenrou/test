FROM registry.lisong.pub:5000/sunrise/maven:3.6.1-jdk-8-211-jenkins as builder
COPY --chown=1000:1000 . /build/
WORKDIR /build
ARG SONAR_KEY
ARG SONAR_VERSION
ARG SONAR_HOST
ARG SONAR_TOKEN
USER jenkins
RUN mvn package sonar:sonar \
  -Dsonar.projectKey=${SONAR_KEY}  \
  -Dsonar.projectVersion=${SONAR_VERSION} \
  -Dsonar.host.url=${SONAR_HOST} \
  -Dsonar.login=${SONAR_TOKEN}

FROM registry.lisong.pub:5000/sunrise/java:8-jdk-211
VOLUME /tmp
ARG JAR_FILE
COPY --from=builder /build/${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["/run-java.sh"]
