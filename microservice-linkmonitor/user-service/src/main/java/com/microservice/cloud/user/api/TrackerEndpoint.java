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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 监控的Endpoint
 * 停用，改用zipkin监控链路
 * @author chzhyang
 * @since 1.0.0
 */
@RestController
@RequestMapping("/tracker")
public class TrackerEndpoint {

    /**
     * 获取服务器被访问次数, /tracker路径的除外
     * @return
     */
//    @RequestMapping(value = "/accessCount", method = RequestMethod.GET)
//    public String accessCount(){
//        return "access count = " + UserEndpoint.accessCount;
//    }
}
