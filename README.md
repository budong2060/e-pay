# e-pay
## 支付系统
    集成微信、支付宝等支付
### 微信支付
    微信公众号、微信app、扫码
### 支付宝
    支付宝网页、支付宝app、支付宝wap

## 配置https
### 生产证书
    通过jdk自带的keytool生成：
        keytool -genkey -alias epay -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore D:\workplace\e-pay\pay-assemble\src\main\resources\epay.p12 -validity 3650
        application.properties 文件中配置https证书相关信息
        通过PayApplication编码同时支持http
## 服务启动
  java -jar -Dspring.active.profile=dev epay.jar

## 整合jsp启动注意
   jsp目录结构
   e-pay
    --pay-assemble
      --src
        --main
          --webapp
             --WEB-INF - jsp
   需进入pay-assemble使用命令mvn spring-boot:run -Dspring.active.profile=dev启动，
   spring DispatcherServlet才能找到对应的页面。