/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.microservice.cloud.product.api;

import com.microservice.cloud.product.config.ApplicationContextProvider;
import com.microservice.cloud.product.config.RibbonConfig;
import com.microservice.cloud.product.entity.ProductComment;
import com.microservice.cloud.product.repository.ProductRepository;
import com.microservice.cloud.product.entity.Product;
import com.microservice.cloud.product.repository.ProductCommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.named.NamedContextFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品管理的Endpoint
 *
 * @author chzhyang
 * @since 1.0.0
 */

@RestController
@RequestMapping("/products")
public class ProductEndpoint {
    protected Logger logger = LoggerFactory.getLogger(ProductEndpoint.class);

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductCommentRepository productCommentRepository;

    @Autowired
    @Qualifier(value = "restTemplate")
    private RestTemplate restTemplate;

    /**
     * 获取商品列表
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Product> list() {
        return this.productRepository.findAll();
    }

    /**
     * 获取指定商品的详情
     * @param id 商品的Id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Product detail(@PathVariable Long id){
        return this.productRepository.findOne(id);
    }

    /**
     * 获取指定商品的评论列表
     * @param id 商品的Id
     * @return
     */
    @RequestMapping(value = "/{id}/comments", method = RequestMethod.GET)
    public List<ProductCommentDto> comments(@PathVariable Long id, @RequestParam Integer lbRule, @RequestParam Integer getCount){
        //加载负载均衡策略

        //@Autowired
        //ApplicationContext applicationContext;

        String newRule = "com.netflix.loadbalancer.RandomRule";//默认策略，随机
        if(lbRule==null || lbRule<=0 || lbRule>3){
            this.logger.debug("lbRule should be set in {1, 2, 3}.");
            return Collections.emptyList();
        }
        if(lbRule==1){//轮询
            newRule = "com.netflix.loadbalancer.RoundRobinRule";
            this.logger.debug("####rule changed to : {}", "round");
        }else if(lbRule==2){//随机
            newRule = "com.netflix.loadbalancer.RandomRule";
            this.logger.debug("####rule changed to : {}", "random");
        }else if(lbRule==3) {//todo 手动实现一致性hash
            newRule = "com.microservice.cloud.product.api.ConsistentHashRule";
            this.logger.debug("####rule changed to : {}", "hash");
        }
        RibbonConfig.ruleClassName = newRule;
        //Take the bean out of spring context and destroy it
        try {
            NamedContextFactory namedContextFactory1 = (NamedContextFactory) ApplicationContextProvider
                    .getBean("feignContext");
            namedContextFactory1.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        NamedContextFactory namedContextFactory2 = (NamedContextFactory) ApplicationContextProvider
                .getBean("springClientFactory");
        namedContextFactory2.destroy();

        //begin to access server
        if(getCount==null || getCount<=0){
            this.logger.debug("getCount should be set.");
            return Collections.emptyList();
        }

        List<ProductComment> commentList = this.productCommentRepository.findByProductIdOrderByCreated(id);

        if (null == commentList || commentList.isEmpty())
            return Collections.emptyList();

        return commentList.stream().map((comment) -> {
            ProductCommentDto dto = new ProductCommentDto(comment);
            dto.setProduct(this.productRepository.findOne(comment.getProductId()));
            dto.setAuthor(this.loadUser(comment.getAuthorId(), getCount));
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 通过RestTemplate加载评论作者的用户信息
     * 进行count次的负载均衡查询，并把每次（port不同）查到的userDto放入userDtoList
     * @param userId 用户Id
     * @return
     */
    protected List<UserDto> loadUser(Long userId, int getCount) {
        List<UserDto> userDtoList= new ArrayList<>();
        for(int i=0; i<getCount; i++){
            UserDto userDto = this.restTemplate.getForEntity("http://USERSERVICE/users/{id}", UserDto.class, userId).getBody();
            if (userDto != null) {

                this.logger.debug("count=" + i +"& I came from server : {}", userDto.getUserServicePort());
            }
            userDtoList.add(userDto);
        }

        return userDtoList;
    }

}