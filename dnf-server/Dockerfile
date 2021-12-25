# Base Image
FROM 1995chen/centos:7.9

MAINTAINER XanderYe

# 定义默认环境变量
ENV MYSQL_IP=192.168.1.2
ENV PUBLIC_IP=127.0.0.1
ENV AUTO_PUBLIC_IP=false
ENV GM_ACCOUNT=gm_user
ENV GM_PASSWORD=gm_pass
ENV GM_CONNECT_KEY=763WXRBW3PFTC3IXPFWH
ENV GM_LANDER_VERSION=20180307

# 编译GeoIP
ADD GeoIP-1.4.8.tgz /home
# 添加依赖库
ADD lib.tgz /lib

# 将模板添加到模版目录下[后续容器启动需要先将环境变量替换,再将文件移动到正确位置]
ADD neople /home/template/neople
ADD root /home/template/root
ADD init /home/template/init

# 启动脚本
ADD docker-entrypoint.sh /

# 更新Repo
RUN yum update -y && yum install -y gcc gcc-c++ make zlib-devel libc.so.6 libstdc++ libstdc++.so.6 glibc.i686 libssl.so.6 xulrunner.x86_64 libXtst.x86_64 initscripts && \
    ln -sf /usr/lib64/libssl.so.10 /usr/lib64/libssl.so.6 && ln -sf /usr/lib64/libcrypto.so.10 /usr/lib64/libcrypto.so.6 && \
    cd /home/GeoIP-1.4.8/ && chmod 777 configure && ./configure && make && make install && yum clean all && \
    chmod -R 777 /tmp && mkdir /data

# 切换工作目录
WORKDIR /root
CMD ["/bin/bash", "/docker-entrypoint.sh"]
