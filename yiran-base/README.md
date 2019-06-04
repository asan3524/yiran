# 微服务治理

标签（空格分隔）： base config eureka hystrix zipkin

---

## 目录

-   [1. 目录](#目录)
-   [2. 概述](#概述)
-   [2. 详细说明](#详细说明)
    - [base-config](#base-config)
    - [base-eureka](#base-eureka)
    - [base-turbine](#base-turbine)
    - [base-authorization](#base-authorization)
    - [base-system](#base-system)
    - [base-core](#base-core)
    - [base-web](#base-web)
    - [base-security](#base-security)
    - [base-resource](#base-resource)
    - [base-sso](#base-sso)
    - [base-hystrix](#base-hystrix)
    - [base-feign](#base-feign)
    - [base-ribbon](#base-ribbon)
    - [base-zipkin](#base-zipkin)

## 概述

微服务治理基础服务包含如下服务，详细内容参见服务详细说明，所有服务集群内部访问使用域名方式.
规则如下：`http://servername.namespaces.svc.cluster.local:port`
示例：`http://rabbitmq.base.svc.cluster.local:15672`

|服务名称|port:nodeport|集群规模|服务说明|
|:-|:-|:-:|:-|
|rabbitmq|5672->34672<br>15672->34673|1|默认消息服务|
|base-config|8888->34888|2|集中动态配置服务，要求所有微服务<br>均使用配置管理服务，并且区分测试环境<br>与开发环境|
|base-eureka|8761->34761<br>8761->34762|2|注册中心，使用多service方式集群部署|
|base-turbine|8989->34989|1|服务监控面板及聚合|
|base-zipkin||1|链路跟踪及日志聚合|
|base-authorization|8887->34887|1|鉴权服务|
|base-system|8088->34808|1|用户、角色、资源管理服务|

## 详细说明

---
#### base-config
-   **Version: V.1.0.0**
-   **Features:**
    -   默认端口为8888
    -   配置文件profiles include develop and production，默认为develop使用单机模式，生产和模拟测试环境中使用production
    -   使用git为配置文件仓库
    -   配置文件规则如下：
    servername-profiles.yml(示例：yiran-base-eureka-s1.yml)
    -   使用bus+rabbitmq进行配置更新消息通知
    -   打开bus-refresh端点，使用POST /actuator/bus-refresh刷新配置（webhooks中使用实现自动配置刷新）
    -   /actuator/bus-refresh基于bus通知所有监听的服务
    -   /actuator/bus-refresh/yiran-base-turbine[:port]更新指定的微服务，要求微服务必须配置spring.cloud.bus.ack.destination-service属性，原理是微服务收到请求后判断destination是否匹配
    -   示例：`http://127.0.0.1:8888/actuator/bus-refresh/yiran-base-turbine`

---

#### base-eureka
-   **Version: V.1.0.0**
-   **Features:**
    -   默认端口为8761
    -   配置文件profiles include develop and s1/s2，默认为develop使用单机模式，生产和模拟测试环境中使用s1/s2
    -   使用base-config作为配置文件中心
    -   使用bus+rabbitmq进行配置更新消息通知
    -   集群方式下默认为两个节点，如果需要增加节点，需要扩展profiles；可以模仿s1/s2增加s3...s4...以此类推，注意需要进行自注册

---
#### base-turbine
-   **Version: V.1.0.0**
-   **Features:**
    -   默认端口为8989，管理端口8990
    -   主要用于基于hystrix的微服务调用链路跟踪，监控熔断情况，请求数，错误数等
    -   配置文件profiles include develop and production，默认为develop使用单机模式，生产和模拟测试环境中使用production
    -   修改turbine.app-config: xxx配置可以聚合指定的微服务，xxx为微服务的名称
    -   监控面板访问地址/hystrix
    -   聚合流访问地址/turbine.stream


---
#### base-authorization
-   **Version: V.1.0.0**
-   **Features:**
    -   默认端口为8887
    -   主要用于基于jwt+oauth2的鉴权，资源控制服务，单点登录服务
    -   配置文件profiles include develop and production，默认为develop使用单机模式，生产和模拟测试环境中使用production
    -   基于base-system用户管理服务进行身份认证及鉴权
    -   自定义RedisClientDetailsServiceBuilder借助redis存储ClientDetails信息
    -   自定义RedisTokenRepositoryImpl借助redis存储Token信息
    -   自定义配置:
            
```
配置默认登陆地址及权限例外路径等
security:
    oauth2:
        authorization:
            loginurl: /login
            deniedurl: /errors/403
            matchers: /images/**, /checkcode, /scripts/**, /styles/**            
配置鉴权服务信息
    client:
        client-id: eagleeye
        client-secret: thisissecret
        authorized-grant-types: authorization_code, refresh_token, password, client_credentials
        scope: webclient, mobileclient
```

---
#### base-system
-   **Version: V.1.0.0**
-   **Features:**
    -   默认端口为8080
    -   系统管理微服务，提供用户、角色、权限管理等能力
    -   对外提供标准的SDK访问（system-client）
    -   SDK基于feign+histrix

---
#### base-core    
-   **Version: 0.0.1-SNAPSHOT**
-   **Features:**
    -   基础工具类
    -   基础对象定义


---
#### base-web    
-   **Version: 0.0.1-SNAPSHOT**
-   **Features:**
    -   基础web服务包，基于spring-starter-web
    -   定义基础controller层


---
#### base-security  
-   **Version: V.1.0.0**
-   **Features:**
    -   基础包，抽象微服务保护的入口
    -   为微服务引入@EnableResourceServer
    -   引入此包的微服务对外暴露的能力必须进行令牌校验
    -   与base-resource base-sso互斥
    -   适合最底层服务，只需要校验令牌的情况


---
#### base-resource  
-   **Version: V.1.0.0**
-   **Features:**
    -   基础包，抽象微服务保护的入口
    -   为微服务引入@EnableResourceServer
    -   自定义YiranSecurityInterceptor过滤器，根据配置及权限数据校验令牌及资源权限
    -   与base-resource base-sso互斥
    -   权限数据使用redis作为分布式缓存，后续考虑引入持久化


---
#### base-sso  
-   **Version: V.1.0.0**
-   **Features:**
    -   基础包，抽象微服务保护的入口，要求服务请求者进行登录，未登录将重定向到鉴权服务
    -   为微服务引入@EnableOAuth2Sso
    -   自定义YiranSecurityInterceptor过滤器，根据配置及权限数据校验令牌及资源权限
    -   与base-resource base-sso互斥
    -   权限数据使用redis作为分布式缓存，后续考虑引入持久化
    -   借助spring-security进行安全过滤，防止crsf攻击等
    -   一般用于网关服务


---
#### base-hystrix
-   **Version: 0.0.1-SNAPSHOT**
-   **Features:**
    -   自定义熔断器跨线程调用时header信息copy传递


---
#### base-feign
-   **Version: 1.0.0**
-   **Features:**
    -   feign基础包，copy OAuth2ClientContext到feign上下文


---
#### base-ribbon
-   **Version: 0.0.1-SNAPSHOT**
-   **Features:**
    -   自定义有状态服务调用，在head中增加并解析transaction
    -   自定义负载均衡策略，在transaction存在时根据事务寻址
    -   sleuth链路跟踪使用TraceLoadBalancerFeignClient 
    而一般情况下feign使用LoadBalancerFeignClient，因此需要进行差异化处理
    来保证在使用sleuth情况下有状态服务调用依旧有效


---
#### base-zipkin