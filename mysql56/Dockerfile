FROM mysql:5.6.51

MAINTAINER XanderYe

COPY /init /init
COPY init.sh /docker-entrypoint-initdb.d/
COPY /TeaEncrypt /

ENV ALLOW_IP=127.0.0.1
ENV GAME_PASSWORD=uu5!^%jg
ENV MYSQL_ROOT_PASSWORD=88888888

RUN chmod 777 /init && chmod a+x /docker-entrypoint-initdb.d/init.sh /TeaEncrypt