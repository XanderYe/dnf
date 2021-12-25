# 初始化数据库
if [ -d /init ];
then
cd /init && chmod +x init.sh && ./init.sh
cd / && rm -rf /init
fi


# 重建root, game用户,并限制game只能容器内服务访问
service mysql start --skip-grant-tables

if $AUTO_MYSQL_IP;
then
MYSQL_IP=$(ip addr | awk '/^[0-9]+: / {}; /inet.*global/ {print gensub(/(.*)\/(.*)/, "\\1", "g", $2)}')
echo mysql ip: $MYSQL_IP
ALLOW_IP=${MYSQL_IP%.*}.%
fi


mysql -u root <<EOF
delete from mysql.user;
flush privileges;
grant all privileges on *.* to 'root'@'%' identified by '$DNF_DB_ROOT_PASSWORD';
grant all privileges on *.* to 'game'@'$ALLOW_IP' identified by 'uu5!^%jg';
flush privileges;
select user,host,password from mysql.user;
EOF
# 关闭服务
service mysql stop
service mysql start
# 修改数据库IP和端口 & 刷新game账户权限只允许本地登录
mysql -u root -p$DNF_DB_ROOT_PASSWORD -P 3306 -h 127.0.0.1 <<EOF
update d_taiwan.db_connect set db_ip="$MYSQL_IP", db_port="3306";
select * from d_taiwan.db_connect;
EOF
