# e-pay
### 系统目录
      e-pay
        |--e-pay
        |   |--pay-assemble(启动main模块)
        |   |--pay-web(优化配置)
        |   |--pay-biz(业务处理模块)
        |   |--pay-biz-handler(业务详细处理模块)
        |   |--pay-support(对业务支撑模块—主要处理渠道选择)
        |   |--pay-dal(mybatis相关模块)
        |   |--pay-client(远程调用客户端模块)
        |   |--pay-common(通用模块)
        |   |--pay-test(测试用例模块)

### API接口
    1.支付接口: /pay
      签名方式: 组合所有的支付参数，并根据提供的密钥做md5
      请求方式: post | application/json;charset=UTF-8
      请求内容:
        {
        	"time": 12456,
        	"sign": "fasdfasdfasdfasdfasdfsadf",
        	"payPayment": {
        		"mchId": "999999",
        		"userId": "234234",
        		"orderNo": "10000006",
        		"orderType": 1,
        		"payWay": 12,
        		"tradeAmount": 20,
        		"requestId": "",
        		"tradeDesc": "test pay",
        		"notifyUrl": "www.notify.com"
        	}
        }
      返回结果：
        根据不同渠道返回不同预支付相关json数据，由前端拉起支付

    2.退费接口: /refund



### 支付系统
    集成微信、支付宝等支付
#### 微信支付
    微信公众号、微信app、扫码
#### 支付宝
    支付宝网页、支付宝app、支付宝wap

### 配置https
#### 生产证书
    通过jdk自带的keytool生成：
        keytool -genkey -alias epay -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore D:\workplace\e-pay\pay-assemble\src\main\resources\epay.p12 -validity 3650
        application.properties 文件中配置https证书相关信息
        通过PayApplication编码同时支持http
### 服务启动
  java -jar -Dspring.active.profile=dev epay.jar

### 整合jsp启动注意
   jsp目录结构
   e-pay
    --pay-assemble
      --src
        --main
          --webapp
             --WEB-INF - jsp
   需进入pay-assemble使用命令mvn spring-boot:run -Dspring.active.profile=dev启动，
   spring DispatcherServlet才能找到对应的页面。