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
    -   微服务监控及聚合服务实现（hystrix+turbine），使用动态配置服务
    -   微服务熔断，及自定义策略（增强hystrix+feign），使用动态配置服务
    -   基于微服务的服务跟踪实现（sleuth+zipkin），使用动态配置服务
    -   日志分析服务，使用动态配置服务，并基于docker发布（ELK）使用动态配置服务
    
  [1]: https://raw.githubusercontent.com/asan3524/yiran/master/static/images/%E5%9F%BA%E7%A1%80%E6%8A%80%E6%9C%AF%E6%9E%B6%E6%9E%84.jpg
