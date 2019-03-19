# 基础架构演进

标签（空格分隔）： base Features

---

## 目录

-   [1. 目录](#目录)
-   [2. 整体架构](#整体架构)
-   [3. 版本计划](#版本计划)
    -   [第一轮](#第一轮)
        -   [V.0.0.1](#001周)
        -   [V.0.0.2](#002周)
    -   [第二轮](#第二轮)
        -   [V.0.0.3](#003周)
        -   [V.1.0.0](#004周)

## 

## 整体架构
根据目前设计，基础架构充分借助spring cloud体系，保障未来五年左右的产品技术架构支撑。

基础技术栈架构图如下：
![技术架构图][1]

## 版本计划

### 第一轮

#### 001周
-   **Version: V.0.0.1**
-   **Time: 2019-03-18 to 2019-03-22**
-   **Features:**
    -   动态配置中心服务，并基于docker发布；基于springboot的动态配置使用demo
    -   高可用注册中心，使用动态配置服务，并基于docker发布
    -   基于springboot的JPA+druid最佳实践

#### 002周
-   **Version: V.0.0.2**
-   **Time: 2019-03-25 to 2019-03-29**
-   **Features:**
    -   微服务基于security的oauth2协议鉴权服务实现（security），使用动态配置服务
    -   微服务监控及聚合服务实现（hystrix+turbine+bus），使用动态配置服务
    -   微服务熔断，及自定义策略（增强hystrix+feign），使用动态配置服务
    -   基于微服务的服务跟踪实现（sleuth+zipkin），使用动态配置服务
    -   日志分析服务，使用动态配置服务，并基于docker发布（ELK）使用动态配置服务
   
### 第二轮

#### 003周
-   **Version: V.0.0.3**
-   **Time: 2019-04-01 to 2019-04-04**
-   **Features:**
    -   分布式缓存组件+基于redisson分布式锁组件实现（redis集群模式））
    -   微服务网关（zuul or gateway）选型及最佳实践
    -   基于springboot+security的安全防护最佳实践

#### 004周
-   **Version: V.1.0.0**
-   **Time: 2019-04-08 to 2019-04-12**
-   **Features:**
    -   微服务负载均衡（ribbon + feign）最佳实践
    -   基于JWT令牌的有状态服务调用（扩展JWT令牌并预留扩展接口
    -   微服务脚手架整合
    -   微服务消息(事件)驱动（spring cloud stream + rabbit）实现
    -   微服务接口ssl方案


  [1]: https://raw.githubusercontent.com/asan3524/yiran/master/static/images/%E5%9F%BA%E7%A1%80%E6%8A%80%E6%9C%AF%E6%9E%B6%E6%9E%84.jpg
