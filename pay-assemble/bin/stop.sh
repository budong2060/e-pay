#!/bin/bash
#
# 服务停止shell脚本
# 这里的服务是指不需要web等容器加载的,直接java命令关闭
#
#

SERVICE_BIN_DIR=$(dirname $(readlink -f "$0"))
SERVICE_HOME=$(dirname $SERVICE_BIN_DIR)

#启动服务的服务名和端口号,一般建议配在配置文件当中,启动和关闭时方便处理
#服务名称和部署目录名称一般保持一致吧
SERVICE_NAME=$(basename $SERVICE_HOME)

if [ -z "$SERVICE_NAME" ]; then
    SERVICE_NAME=$(hostname)
fi

echo "begin stopping $SERVICE_NAME"

#找到服务对应的pid来停掉,根据服务名或者端口号来找都可以吧
PID=`ps -ef|grep "$SERVICE_HOME"|grep -v "grep"|awk '{print $2}'|head -n 1`

if [ -z "$PID" ]; then
    echo "ERROR: The $SERVICE_NAME does not started!"
    exit 1
fi

echo "Find server name[$SERVICE_NAME] macth PID is:$PID"
echo -e "Stopping the $SERVICE_NAME ..."

#kill target pid
kill -9 $PID

echo "Stopped $SERVICE_NAME [$PID] Done!!!"
