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
package com.microservice.cloud.user.api;

import com.microservice.cloud.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 用户管理的Endpoint
 *
 * @author chzhyang
 * @since 1.0.0
 */
@RestController
@RequestMapping("/users")
public class UserEndpoint {
    @Autowired
    private UserService userService;

    @Value("${server.port}")
    protected static int serverPort = 0;

    @Autowired
    @Qualifier(value = "restTemplate")
    private RestTemplate restTemplate;

    public static int accessCount = 0;
    /**
     * 获取用户列表
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<UserDto> findAll(){
        accessCount++;
        return this.userService.findAll();
    }

    /**
     * 获取用户详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserDto detail(@PathVariable Long id){
        accessCount++;
        return this.userService.load(id);
    }

    /**
     * 更新用户详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public UserDto update(@PathVariable Long id, @RequestBody UserDto userDto){
        accessCount++;
        //this.notice(); //改用zipkin监控链路
        return this.userService.save(userDto);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public boolean delete(@PathVariable Long id){
        accessCount++;
        this.userService.delete(id);
        return true;
    }

//    public void notice(){
//        String url = "http://localhost:8280/notices/" + serverPort + "?accessCount=" + accessCount;
//        this.restTemplate.getForEntity(url, null);
//    }
}
