package com.microservice.cloud.product.config;

import com.microservice.cloud.product.api.ProductEndpoint;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import com.netflix.loadbalancer.IRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.netflix.ribbon.PropertiesFactory;

/**
 * 将RestTemplate 和 Ribbon 相结合进行负载均衡
 */
@EnableHystrixDashboard
@EnableCircuitBreaker
@Configuration
@RibbonClients(defaultConfiguration = RibbonConfig.class)
public class RibbonConfig {
    /**
     * 更改 负载均衡策略算法
     * RandomRule #配置规则 随机
     * RoundRobinRule #配置规则 轮询
     * RetryRule #配置规则 重试
     * WeightedResponseTimeRule #配置规则 响应时间权重
     * 其他自定义负载均衡策略的类
     * @return
     */

    protected Logger logger = LoggerFactory.getLogger(ProductEndpoint.class);

    @Autowired(required = false)
    private IClientConfig config;

    @Autowired
    private PropertiesFactory propertiesFactory;

//    @Autowired
//    private LoadBalancerService loadBalancerService;

    //默认使用随机策略，方便测试
    public static String ruleClassName = "com.netflix.loadbalancer.RandomRule";

    @Bean(value = "restTemplate")
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    public IRule ribbonRule(){
        AbstractLoadBalancerRule rule = null;
        try {
            rule = (AbstractLoadBalancerRule) Class.forName(ruleClassName).newInstance();
            rule.initWithNiwsConfig(config);
            this.logger.debug("Rule change to " + ruleClassName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rule;
    }
}
