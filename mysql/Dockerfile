# Base Image
FROM 1995chen/centos:7.9

MAINTAINER XanderYe

# ALLOW_IP会使用内网IP网段，dnf-mysql和dnf-server使用同一个网桥时开启
ENV AUTO_ALLOW_IP=true
# dnf服务端game用户连接mysql的ip白名单，公网开启
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