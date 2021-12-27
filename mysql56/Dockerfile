FROM mysql:5.6.51

MAINTAINER XanderYe

COPY init /init
COPY setup.sh /

ENV ALLOW_IP=127.0.0.1

CMD [ "sh" , "/setup.sh" ]