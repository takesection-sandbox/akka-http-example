FROM    alpine

ARG     JAR_NAME
ENV     JAR_NAME ${JAR_NAME}

COPY    modules/root/target/scala-2.12/${JAR_NAME} /var
WORKDIR /var

RUN     apk --update add openjdk8-jre
EXPOSE  8080

CMD     /usr/bin/java -jar ${JAR_NAME}
