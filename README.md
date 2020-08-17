# Goals

- [x] 服务发现和注册
  - [x] erueka
- [x] 负载均衡
  - [x] ribbon
  - [x] feign
- [x] 链路监控
  - [x] sleuth
  - [x] zipkin
- [ ] 健康监控和容灾
  - [ ] hystrix
- [ ] API网关
  - [ ] zuul
  - [ ] 关闭product手动负载均衡，再次尝试Hystrix健康监控
  - [ ] API网关负载均衡策略动态选择
- [ ] 配置中心
  - [ ] config
- [ ] 消息机制
  - [ ] kafka
  - [ ] 缓存
- [ ] docker部署
  - [ ] Jenkins持续部署
  - [ ] kubernetes 服务编排、监控和自动恢复、水平扩展


## Finished

1. 商品、用户微服务
2. eureka，服务发现
3. ribbon+feign，商品端负载均衡，且可通过http选择策略
4. sleuth+zipkin，服务访问链路监控

