FROM openjdk:18-jdk-alpine
RUN apk add --update make
COPY . /app
WORKDIR /app
RUN make build
EXPOSE 8080
ENTRYPOINT ["make", "run"]
