#!/bin/bash
#
# 服务启动shell脚本
# 这里的服务是指不需要web等容器加载的,直接java命令启动
#
#

# 路径探测区 - 请勿修改
# =====================================
SERVICE_BIN_DIR=$(dirname $(readlink -f "$0"))
SERVICE_HOME=$(dirname $SERVICE_BIN_DIR)


# 手动设置区 - 运维填写
# =====================================
# JDK HOME路径
JAVA_HOME=/usr/java/latest
SERVICE_PORT=9099

#配置及日志目录
CONF_DIR=$SERVICE_HOME/conf
LOGS_DIR=$SERVICE_HOME/logs
#控制台日志
STDOUT_FILE=$LOGS_DIR/stdout.log

# 启动flag
JAVA_JMX_ENABLE=YES

# JMX配置
JMX_PORT=8090


# 手动设置区 - 研发填写
# =====================================
#程序运行主类
SERVICE_MAIN=com.pay.boot.PayApplication
SERVICE_DEBUG_PORT=8077
# 启动flag
JAVA_DEBUG_ENABLE=NO


# =====================================
# 运行环境设置
RUN_ENV="dev"
read -t 30 -p "请输入运行环境:" RUN_ENV
echo -e "\n"
echo "运行环境":$RUN_ENV


# 预探测服务存活
# =====================================
#判断该服务名称是否已经启动加载了
PIDS=$(ps --no-heading -C java -f --width 1000 | grep $SERVICE_HOME |awk '{print $2}')
if [ -n "$PIDS" ]; then
    echo "ERROR: The $SERVICE_NAME already started!"
    echo "PID: $PIDS"
    exit 1
fi

#判断端口号有没有被占用
if [ -n "$SERVICE_PORT" ]; then
    SERVICE_PORT_COUNT=`netstat -tln | grep $SERVICE_PORT | wc -l`
    if [ $SERVICE_PORT_COUNT -gt 0 ]; then
        echo "ERROR: The $SERVICE_NAME port $SERVICE_PORT already used!"
        exit 1
    fi
fi

# 自动设置区
# =====================================
#服务名称和部署目录名称一般保持一致吧
SERVICE_NAME=$(basename $SERVICE_HOME)
if [ -z "$SERVICE_NAME" ]; then
    SERVICE_NAME=$(hostname)
fi

#所有依赖到的jar文件
LIB_DIR=$SERVICE_HOME/lib
LIB_JARS=$(ls $LIB_DIR | grep ".jar" |awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":")

#建立日志目录
mkdir -p $LOGS_DIR

# 初始化结束，打印初始化信息
# =====================================
echo "current deploy-dir is : $SERVICE_HOME"
echo "start server use main class : $SERVICE_MAIN"
echo "init server name[$SERVICE_NAME],server port[$SERVICE_PORT]"


# JAVA启动选项初始化区
# =====================================
# java 常规启动
#JAVA_OPTS=" -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dlog.path=${LOGS_DIR} -Dservice.port=${SERVICE_PORT}  -Djmx.port=${JMX_PORT} "
JAVA_OPTS=" -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dlog.path=${LOGS_DIR} -Djmx.port=${JMX_PORT} -Dspring.active.profile=${RUN_ENV} "


# 默认启动内存配置
JAVA_MEM_OPTS=""
BITS=$(file $JAVA_HOME/bin/java | grep "64-bit")
if [ -n "$BITS" ]; then
    let memTotal=$(cat /proc/meminfo |grep MemTotal|awk '{printf "%d", $2/1024 }')
    if [ $memTotal -gt 2500 ];then
        #JAVA_MEM_OPTS=" -server -Xmx4g -Xms4g -Xmn256m -Xss512k -XX:+DisableExplicitGC  -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -Duser.timezone=GMT+8 "
        JAVA_MEM_OPTS=" -server -Xmx256m -Xms256m -Xmn128m -Xss512k -XX:+DisableExplicitGC  -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -Duser.timezone=GMT+8 "
    else
        #JAVA_MEM_OPTS=" -server -Xmx256m -Xms256m -Xmn128m -Xss512k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "
        JAVA_MEM_OPTS=" -server -Xmx256m -Xms256m -Xmn128m -Xss512k -XX:+DisableExplicitGC  -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -Duser.timezone=GMT+8 "
    fi
else
    JAVA_MEM_OPTS=" -server -Xms256m -Xmx256m -XX:SurvivorRatio=2 -XX:+UseParallelGC "
fi

# debug 启动flag
JAVA_DEBUG_OPTS=""
if [ $JAVA_DEBUG_ENABLE = YES ]; then
    JAVA_DEBUG_OPTS=" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=${SERVICE_DEBUG_PORT},server=y,suspend=n "
fi

# JMX 监听flag
JAVA_JMX_OPTS=""
if [ $JAVA_JMX_ENABLE = YES ]; then
    if [ $JMX_PORT ]; then
        JAVA_JMX_OPTS=" -Dcom.sun.management.jmxremote.port=${JMX_PORT} -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false "
    fi
fi


# 整合JAVA启动参数
JAVA_EXEC="$JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS -classpath $CONF_DIR:$LIB_JARS $SERVICE_MAIN"
echo -e "应用参数配置目录: $CONF_DIR ...\c"
echo -e "==============================================================================================================\c"
echo -e "系统启动参数: $JAVA_OPTS ...\c"
echo -e "系统VM启动参数: $JAVA_MEM_OPTS...\c"
echo -e "系统DEBUG启动参数: $JAVA_DEBUG_OPTS...\c"
echo -e "系统JMX监听配置参数: $JAVA_JMX_OPTS...\c"


# 正式启动区
# =====================================
echo -e "Starting the $SERVICE_NAME ...\c"
#nohup $JAVA_HOME/bin/java $JAVA_EXEC > $STDOUT_FILE 2>&1 &
# 不输出日志，交由应用处理
nohup $JAVA_HOME/bin/java $JAVA_EXEC > /dev/null 2>log &


# 启动后检测
# =====================================
COUNT=0
while [ $COUNT -lt 1 ]; do
    echo -e ".\c"
    sleep 1
       COUNT=$(ps --no-heading -C java -f --width 1000 | grep "$SERVICE_HOME" | awk '{print $2}' | wc -l)
    if [ $COUNT -gt 0 ]; then
        break
    fi
done
echo "OK!"
PIDS=$(ps --no-heading -C java -f --width 1000 | grep "$SERVICE_HOME" | awk '{print $2}')
echo "PID: $PIDS"
echo "STDOUT: $STDOUT_FILE"

