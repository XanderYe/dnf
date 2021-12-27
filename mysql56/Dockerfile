FROM mysql:5.6.51

MAINTAINER XanderYe

COPY /init /init
COPY init.sh /docker-entrypoint-initdb.d/

ENV ALLOW_IP=127.0.0.1
ENV MYSQL_ROOT_PASSWORD=88888888

RUN chown -R mysql:mysql /init && chmod a+x /docker-entrypoint-initdb.d/init.sh