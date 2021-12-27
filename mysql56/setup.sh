# 初始化数据库
if [ -d /init ];
then
cd /init && chmod +x init.sh && ./init.sh
cd / && rm -rf /init
fi

service mysql stop

mysqld --user=mysql --skip-grant-tables &

mysql -u root <<EOF
delete from mysql.user;
flush privileges;
grant all privileges on *.* to 'root'@'%' identified by '$MYSQL_ROOT_PASSWORD';
grant all privileges on *.* to 'game'@'$ALLOW_IP' identified by 'uu5!^%jg';
flush privileges;
select user,host,password from mysql.user;
EOF
# 关闭服务
service mysql stop
service mysql start
# 修改数据库IP和端口 & 刷新game账户权限只允许本地登录
mysql -u root -p$MYSQL_ROOT_PASSWORD -P 3306 -h 127.0.0.1 <<EOF
update d_taiwan.db_connect set db_ip="127.0.0.1", db_port="3306";
select * from d_taiwan.db_connect;
EOF

tail -f /dev/null