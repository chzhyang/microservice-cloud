# sleuth
分布式链路监控：trace+span

Sleuth所生成的追踪数据，它们的格式为[ApplicationName, TraceId, SpanId,Exportable]

>  这里，ApplicationName的值为productservice，是Sleuth当前所追踪服务的服务名称

# zipkin
## 定义

采集一个外部请求所跨多个微服务之间的服务跟踪数据，同时以可视化的方式为开发者展现服务请求所跨越多个微服务中耗费的总时间及各个微服务所耗费的时间。

ZipKin并不是Spring Cloud下的一个子项目，开源地址为https://github.com/openzipkin/zipkin

## 效果

每次服务调用都包含了下面几种数据：

1. 调用总耗时

2. 本次调用所产生的Span个数

3. 本次调用中占用耗时最多的服务及所占百分比

4. 本次调用中所调用的微服务列表及被调用次数

![截屏2020-08-16 16.20.30](README.assets/PNxUhifSmFayWOA.png)



本次调用拆分的Span列表，以及每一个Span相应的耗时和相应请求的服务地址

![截屏2020-08-16 16.21.15](README.assets/4FQdmU3OuyHYbZ6.png)

每个span的详细情况

![截屏2020-08-16 16.23.04](README.assets/1pod7iwM8FWf5y2.png)



通过Dependencies标签查看服务请求中各微服务之间的依赖关系

![截屏2020-08-16 16.27.40](README.assets/35lA8riZmSUHpOY.png)

当调用异常并且没有捕获时，Zipkin就会自动将本次调用标记为红色

![截屏2020-08-16 16.29.39](README.assets/bNsgpALQRdH9yZf.png)