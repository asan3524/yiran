# 分布式缓存与锁设计与实现

标签（空格分隔）： 分布式缓存 分布式锁 redis redisson spring


---
## 目录

-   [1. 目录](#目录)
-   [2. 思路](#思路)
-   [3. 已完成](#已完成)
-   [4. 依赖](#依赖)


## 思路
 本组件借助spring-redis-data+jedis+redisson实现分布式缓存和锁
 
 - 基于spring配置，借助@ConditionalOnProperty适配多种redis部署方式，要求各种适配方式的XxxProperties使用@Configuration("redisProperties")注解，以便外部使用@ConditionalOnResource(resources="redisProperties")验证redis是否加载

    - Standalone：必须配置spring.redis.host
    - Cluster：必须配置spring.redis.cluster.nodes
    - Sentinel：必须配置xxx
 
 - 序列化：
    - key使用StringRedisSerializer序列化
    - value使用Jackson2JsonRedisSerializer序列化，并借助alibaba fastjson

 - 对外接口
    - RedisCacheComponent 分布式缓存接口实现
        - KV
        - KHash
        - KList
        - KSet
    - RedisLockComponent 分布式锁接口实现

---

## 已完成

- redis部署适配
    - Standalone
    - Cluster

- RedisCacheComponent
    - KV
    - KHash

---
## 依赖

 - spring-data-redis
 - redis.clients.jedis 
 - redisson 3.9.1
 - alibaba fastjson 1.2.54






