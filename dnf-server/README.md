# dnf服务端

## 说明

1. 使用`libhook.so`优化了CPU占用,1-2分钟出五国，占用10%以内
2. game密码限制小于等于8位，否则无法连接
3. 修改环境变量重启即刻生效

## 启动

详见[docker-compose.yml](docker-compose.yml)

## 环境变量

AUTO_MYSQL_IP: 和dnfmysql使用统一自定义网桥使用，支持自动获取mysql的ip

MYSQL_NAME: dnfmysql容器名，用户自动获取mysql的ip

MYSQL_IP: mysql的ip

MYSQL_PORT: mysql的端口

GAME_PASSWORD: game账户密码，小于等于8位

AUTO_PUBLIC_IP: 自动获取公网ip

PUBLIC_IP: 你的ip地址

GM_ACCOUNT: GM账户名

GM_PASSWORD: GM密码

GM_CONNECT_KEY: 网关KEY

GM_LANDER_VERSION: 网关版本