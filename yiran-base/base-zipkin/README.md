# ELK部署与使用

## zipkin使用

<font color='orange'>zipkin在`springcloud2.x`已不推荐自己创建`Zipkin Server`服务, 需要使用编译好的jar</font>

- 安装zipkin docker

```shell
docker run -d -p 9411:9411 openzipkin/zipkin
```

- 添加依赖

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

- yml配置

```yml
spring: 
  sleuth: 
    feign: 
      enabled: true
    sampler: 
      # 采样率, 1.0表示全量采集
      probability: 1.0
    web: 
      client: 
        enabled: true
  zipkin: 
    enabled: true
    # zipkin的部署地址
    base-url: http://192.168.80.81:9411/
    service: 
      name: ${spring.application.name}
    sender: 
      # 支持http, rabbitmq, kafka, 后期采用kafka
      type: web   
```

- zipkin访问地址

部署在哪台机器请自行修改IP
```yml
http://192.168.80.81:9411/
```

## ELK部署安装

- 下载镜像

```shell
docker pull sebp/elk
```
- 运行条件

这是一个聚合镜像, 要求Docker至少得分配3GB的内存; Elasticsearch至少需要单独2G的内存; `vm.max_map_count`至少需要`262144`
```shell
# linux按如下操作, 后期重新制作镜像 
sudo vi /etc/sysctl.conf
# 添加如下
vm.max_map_count=262144
# 查看是否生效
sysctl -p

```

- 启动`ELK` 

|- 端口  |- 作用 |
:----:|:----
`5601`|Kibana映射地址
`9200`|ES映射地址
`5044`|Logstash Beats界面映射地址
`9250`|Logstash监听搜集日志的端口(可自行随意指定)<br>目前使用tcp方式从服务节点发送日志到logstash, 不采用物理文件日志搜集方式, 后期会添加kafka队列作为缓冲
```shell
### 注意挂载配置文件目录
docker run -itd -p 9250:9250 -p 5601:5601 -p 9200:9200 -p 5044:5044 -v /Users/apple/idea/spring-cloud-bingo/config:/data --name elk sebp/elk:latest 
```

稍微注意下, 由于需要配置`logstash`如何在`elasticsearch`创建索引, 所以需要定义`logstash.conf`文件, 在`ELK`启动时需要指定, 配置文件如下

- `logstash.conf` 配置

```conf
# For detail structure of this file
# Set: https://www.elastic.co/guide/en/logstash/current/configuration-file-structure.html
input {
  # For detail config for log4j as input,
  # See: https://www.elastic.co/guide/en/logstash/current/plugins-inputs-log4j.html
  tcp {
    mode => "server"
    host => "127.0.0.1"
    port => 9250 #logstash监听地址
    codec => "json" #发送格式需是json
  }
}
filter {
  ## 过滤条件后期添加, 也可以在logback.xml中预先过滤一次
  #Only matched data are send to output.
}
output {
  # For detail config for elasticsearch as output,
  # See: https://www.elastic.co/guide/en/logstash/current/plugins-outputs-elasticsearch.html
  elasticsearch {
    action => "index"          #The operation on ES
    hosts  => ["127.0.0.1:9200"] #ElasticSearch host, can be array.
    index  => "applog"         #The index to write data to.
  }
}
```

- 进入容器重启`logstash`
目的是让`logstash`应用`logstash.conf`配置文件
<font color='red'> 此处需要重新制作镜像, 将启动配置`logstash.conf`写入Dockerfile</font>

```shell
docker exec -it elk /bin/bash
#停止logstash
service logstash stop
#启动测试是否与es连接
/opt/logstash/bin/logstash -e 'input { stdin { } } output { elasticsearch { hosts => ["localhost"] } }'
#随便在控制台输入字符, 然后进es地址查看
#localhost:9200/_search?pretty

#最后带配置启动logstash
/opt/logstash/bin/logstash --path.data /tmp/logstash/data -f /data/logstash.conf
```

## SpringCloud与Logstash集成
通过将日志模块搜集的日志以

- 依赖

```xml
<!-- 支持tcp和udp -->
<dependency>
	<groupId>net.logstash.logback</groupId>
	<artifactId>logstash-logback-encoder</artifactId>
	<version>5.3</version>
</dependency>
```

- logback.xml

详情请见工程

- 配置

<font color='red'>注意此节点需要配置到 `bootstrap.yml`;
同时`logback.remote.xml`名字固定, 否则导致日志xml配置提前解析, 会找不到`logstash.tcp.destination`填写的值</font>
```yml
logstash: 
  tcp:
    destination: 127.0.0.1:9250
logging: 
  config: classpath:logback.remote.xml
```

## 后期需要做的事情

1. sleuth与elk之间添加kafka
2. 镜像重新制作, 将配置打入镜像

备注:
对ELK若有疑问, 可自行参考 [elk-docker
](https://elk-docker.readthedocs.io/)