# 删除无用文件
rm -rf /home/template/neople-tmp
rm -rf /home/template/root-tmp
mkdir -p /home/neople
# 清理root下文件
rm -rf /root/DnfGateServer
rm -rf /root/GateRestart
rm -rf /root/GateStop
rm -rf /root/run
rm -rf /root/stop
rm -rf /root/Config.ini
rm -rf /root/privatekey.pem

# 复制待使用文件
cp -r /home/template/neople /home/template/neople-tmp
cp -r /home/template/root /home/template/root-tmp

# 检查/data
/home/template/init/init.sh

# 获取mysql容器的ip
if $AUTO_MYSQL_IP;
then
  MYSQL_IP=`ping -i 0.1 -c 1 $MYSQL_NAME|sed '1{s/[^(]*(//;s/).*//;q}'`
  echo mysql ip: $MYSQL_IP
fi

echo "0.0.0.0 3306 $MYSQL_IP $MYSQL_PORT" > /etc/rinetd.conf
rinetd -c /etc/rinetd.conf

# 获取公网ip
if $AUTO_PUBLIC_IP;
then
  PUBLIC_IP=`curl -s http://pv.sohu.com/cityjson?ie=utf-8|awk -F\" '{print $4}'`
  echo public ip: $PUBLIC_IP
fi

sleep 2

# 替换环境变量
sed -i "s/PUBLIC_IP/$PUBLIC_IP/g" `find /home/template/neople-tmp -type f -name "*.cfg"`
sed -i "s/PUBLIC_IP/$PUBLIC_IP/g" `find /home/template/neople-tmp -type f -name "*.tbl"`
# 将结果文件拷贝到对应目录[这里是为了保住日志文件目录,将日志文件挂载到宿主机外,因此采用覆盖而不是mv]
cp -rf /home/template/neople-tmp/* /home/neople
rm -rf /home/template/neople-tmp
# 复制版本文件
cp /data/Script.pvf /home/neople/game/Script.pvf
chmod 777 /home/neople/game/Script.pvf
cp /data/df_game_r /home/neople/game/df_game_r
chmod 777 /home/neople/game/df_game_r
cp /data/publickey.pem /home/neople/game/

mv /home/template/root-tmp/* /root/
rm -rf /home/template/root-tmp
# 拷贝证书key
cp /data/privatekey.pem /root/
# 构建配置文件软链[不能使用硬链接, 硬链接不可跨设备]
ln -s /data/Config.ini /root/Config.ini
# 替换Config.ini中的GM用户名、密码、连接KEY、登录器版本[这里操作的对象是一个软链接不需要指定-type]
sed -i --follow-symlinks "6c IP=$MYSQL_IP" `find /root -name "*.ini"`
sed -i --follow-symlinks "8c Port=$MYSQL_PORT" `find /root -name "*.ini"`
sed -i --follow-symlinks "s/GM_ACCOUNT/$GM_ACCOUNT/g" `find /root -name "*.ini"`
sed -i --follow-symlinks "s/GM_PASSWORD/$GM_PASSWORD/g" `find /root -name "*.ini"`
sed -i --follow-symlinks "s/GM_CONNECT_KEY/$GM_CONNECT_KEY/g" `find /root -name "*.ini"`
sed -i --follow-symlinks "s/GM_LANDER_VERSION/$GM_LANDER_VERSION/g" `find /root -name "*.ini"`

cd /root
# 启动服务
./run
