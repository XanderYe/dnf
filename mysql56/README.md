# 基于官方mysql5.6的dnf数据库

## 说明

1. 与5.0互不兼容
2. 首次运行会导入数据，该过程耗时较长，可能会超过10分钟请耐心等待，使用`docker logs 容器名`查看日志出现很多行 表名ip端口密文 即完成
3. 只在首次启动执行初始化脚本，后续修改密码需要手动执行sql

## 启动

详见[docker-compose.yml](docker-compose.yml)

## 环境变量

ALLOW_IP: game账户白名单ip，建议使用docker内网网段如`172.20.0.%`；修改game密码后可开放`%`

MYSQL_ROOT_PASSWORD: mysql root密码

## 修改root密码

连接数据库，执行sql
```sql
grant all privileges on *.* to 'root'@'%' identified by '新密码';
flush privileges;
```

## 修改game密码

连接数据库，执行sql
```sql
grant all privileges on *.* to 'game'@'%' identified by '新密码';
flush privileges;
# 使用TEA算法计算48位密文
update d_taiwan.db_connect set db_passwd="密文";
```