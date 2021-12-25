# Base Image
FROM 1995chen/centos:7.9

MAINTAINER XanderYe

# 编译GeoIP
ADD GeoIP-1.4.8.tgz /home
# 添加依赖库
ADD lib.tgz /lib

# 更新Repo
RUN yum update -y && yum install -y gcc gcc-c++ make zlib-devel libc.so.6 libstdc++ libstdc++.so.6 glibc.i686 libssl.so.6 xulrunner.x86_64 libXtst.x86_64 initscripts && \
    ln -sf /usr/lib64/libssl.so.10 /usr/lib64/libssl.so.6 && ln -sf /usr/lib64/libcrypto.so.10 /usr/lib64/libcrypto.so.6 && \
    cd /home/GeoIP-1.4.8/ && chmod 777 configure && ./configure && make && make install && yum clean all

# 切换工作目录
WORKDIR /root
