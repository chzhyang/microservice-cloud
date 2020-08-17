## Finished
1. 商品、用户微服务
2. eureka，服务发现
3. ribbon+feign，商品端负载均衡，且可通过http选择策略
4. sleuth+zipkin，服务访问链路监控
## Todo
-[ ] zuul API网关
    -[ ] 关闭product手动负载均衡，再次尝试Hystrix健康监控和容灾
    -[ ] 改为API网关负载均衡
-[ ] stream 消息机制
    -[ ] kafka
    -[ ] redis
-[ ] docker
-[ ] k8s