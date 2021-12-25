# Base Image
FROM xanderye/centos7-geoip

MAINTAINER XanderYe

# 定义默认环境变量
ENV AUTO_MYSQL_IP=true
ENV MYSQL_NAME=dnfmysql
ENV MYSQL_IP=192.168.1.2
ENV AUTO_PUBLIC_IP=false
ENV PUBLIC_IP=127.0.0.1
ENV AUTO_PUBLIC_IP=false
ENV GM_ACCOUNT=gm_user
ENV GM_PASSWORD=gm_pass
ENV GM_CONNECT_KEY=763WXRBW3PFTC3IXPFWH
ENV GM_LANDER_VERSION=20180307

# 将模板添加到模版目录下[后续容器启动需要先将环境变量替换,再将文件移动到正确位置]
ADD neople /home/template/neople
ADD root /home/template/root
ADD init /home/template/init

# 启动脚本
ADD docker-entrypoint.sh /

RUN chmod -R 777 /tmp && mkdir /data

# 切换工作目录
WORKDIR /root
CMD ["/bin/bash", "/docker-entrypoint.sh"]
