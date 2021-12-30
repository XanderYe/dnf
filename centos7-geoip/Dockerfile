# Base Image
FROM centos:7.9.2009

MAINTAINER XanderYe

# 编译GeoIP
ADD GeoIP-1.4.8.tgz /home
# 添加依赖库
ADD lib.tgz /lib

RUN sed -e 's|^mirrorlist=|#mirrorlist=|g' \
             -e 's|^#baseurl=http://mirror.centos.org/centos|baseurl=https://mirrors.ustc.edu.cn/centos|g' \
             -i.bak \
             /etc/yum.repos.d/CentOS-Base.repo && sed -i "s|enabled=1|enabled=0|g" /etc/yum/pluginconf.d/fastestmirror.conf && \
    yum clean all && yum makecache && yum install -y psmisc openssl openssl-devel curl wget perl vim && \
    yum install -y gcc gcc-c++ make automake zlib-devel libc.so.6 libstdc++ libstdc++.so.6 glibc.i686 libssl.so.6 xulrunner.x86_64 libXtst.x86_64 initscripts && \
    ln -sf /usr/lib64/libssl.so.10 /usr/lib64/libssl.so.6 && ln -sf /usr/lib64/libcrypto.so.10 /usr/lib64/libcrypto.so.6 && \
    cd /home/GeoIP-1.4.8/ && chmod 777 configure && ./configure && make && make install && yum clean all

# 切换工作目录
WORKDIR /root
