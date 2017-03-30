FROM    alpine
ARG     JAR_NAME
WORKDIR /var
CMD     /usr/bin/java -jar ${JAR_NAME}
