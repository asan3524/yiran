# 基础架构演进

标签（空格分隔）： base Features

---

## 1. 目录

-   [1. 目录](#目录)
-   [2. 整体架构](#整体架构)
-   [3. 版本计划](#版本计划)
    -   [V.1.0](#V.1.0)

## 2. 整体架构
根据目前设计，基础架构充分借助spring cloud体系，保障未来五年左右的产品技术架构支撑。

基础技术栈架构图如下：
![技术架构图][1]

## 3. 版本计划

### 3.1. V.1.0
Time: 2019-03-18 to 2019-03-22
Features:
    - 高可用注册中心，并基于docker发布
    - 动态配置中心服务，并基于docker发布；基于springboot的动态配置使用demo
    - 基于springboot的JPA+druid最佳实践

### 3.1. V.1.0
Time: 2019-03-25 to 2019-03-29
Features:
    - 微服务监控及聚合服务实现（hystrix+turbine）
    - 基于微服务的服务跟踪实现（sleuth）
    - 日志分析服务，并基于docker发布
    
  [1]: https://raw.githubusercontent.com/asan3524/yiran/master/static/images/%E5%9F%BA%E7%A1%80%E6%8A%80%E6%9C%AF%E6%9E%B6%E6%9E%84.jpg
