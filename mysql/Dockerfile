# Base Image
FROM 1995chen/centos:7.9

MAINTAINER XanderYe

# 自动获取内网IP(ALLOW_IP会使用内网IP网段)
ENV AUTO_MYSQL_IP=false
# dnf服务连接的mysql的ip
ENV MYSQL_IP=127.0.0.1
# dnf服务器连接的的端口
ENV MYSQL_PORT=3306
# dnf服务端game用户连接mysql的ip白名单
ENV ALLOW_IP=127.0.0.1

# 安装mysql依赖
ADD MySQL-shared-compat-5.0.95-1.rhel5.x86_64.rpm /tmp
ADD MySQL-devel-community-5.0.95-1.rhel5.x86_64.rpm /tmp
ADD MySQL-client-community-5.0.95-1.rhel5.x86_64.rpm /tmp
ADD MySQL-server-community-5.0.95-1.rhel5.x86_64.rpm /tmp
ADD init /init
ADD docker-entrypoint.sh /

# 更新Repo
RUN yum update -y && yum install -y initscripts && \
    rpm -ivh /tmp/MySQL-shared-compat-5.0.95-1.rhel5.x86_64.rpm && \
    rpm -ivh /tmp/MySQL-devel-community-5.0.95-1.rhel5.x86_64.rpm && \
    rpm -ivh /tmp/MySQL-client-community-5.0.95-1.rhel5.x86_64.rpm && \
    rpm -ivh /tmp/MySQL-server-community-5.0.95-1.rhel5.x86_64.rpm && service mysql stop && \
    rm -rf /var/lib/mysql/* && yum clean all && \
    rm -rf /tmp/MySQL* && chmod -R 777 /docker-entrypoint.sh

VOLUME /var/lib/mysql
EXPOSE 3306

ENTRYPOINT ["/bin/bash","-c","/docker-entrypoint.sh && /bin/bash"]