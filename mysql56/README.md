# 基于官方mysql5.6的dnf数据库

## 说明

1. 与5.0互不兼容
2. game密码限制小于等于8位，否则无法连接
3. 首次运行会导入数据，该过程耗时较长，可能会超过10分钟请耐心等待，使用`docker logs dnfmysql`查看日志出现出现一大堆数据库配置列表才是启动完成
4. 只在首次启动执行初始化脚本，后续修改密码需要手动执行sql

## 启动

详见[docker-compose.yml](docker-compose.yml)

## 环境变量

ALLOW_IP: game账户白名单ip，建议使用docker内网网段如`172.20.0.%`；修改game密码后可开放`%`

GAME_PASSWORD: game账户密码，小于等于8位

MYSQL_ROOT_PASSWORD: mysql root密码

## 修改root密码

连接数据库，执行sql
```sql
grant all privileges on *.* to 'root'@'%' identified by '新密码';
flush privileges;
```

## 修改game密码
1. 计算TEA密文
`docker exec -it dnfmysql /TeaEncrypt 新密码`
2. 连接数据库，执行sql
```sql
grant all privileges on *.* to 'game'@'%' identified by '新密码';
flush privileges;
# 更新48位密文
update d_taiwan.db_connect set db_passwd='密文';
```
