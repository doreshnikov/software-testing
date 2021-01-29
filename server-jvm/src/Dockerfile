FROM gradle:6.7-jdk8

COPY ./.. /manga-reader-server
WORKDIR /manga-reader-server
RUN gradle build --exclude-task test

ENTRYPOINT ["gradle", "run"]
