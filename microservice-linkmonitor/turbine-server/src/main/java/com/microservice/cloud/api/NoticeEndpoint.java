
package com.microservice.cloud.api;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * notice管理的Endpoint
 *
 * @author chzhyang
 * @since 1.0.0
 */
@RestController
@RequestMapping("/notices")
public class NoticeEndpoint {
    private Map<String, Integer> serverMap = new ConcurrentHashMap<>();

    @RequestMapping(value = "/{serverPort}/", method = RequestMethod.GET)
    public void updateServerAccess(@PathVariable String serverPort, @RequestParam Integer accessCount){
        if(serverMap.get(serverPort) > accessCount){
            System.out.println("error accessCount!");
        }else{
            serverMap.put(serverPort, accessCount);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        StringBuilder list = new StringBuilder();
        for(Map.Entry<String, Integer> entry : serverMap.entrySet()){
            list.append(entry.getKey());
            list.append(":");
            list.append(entry.getValue());
            list.append("##");
        }
        return list.toString();
    }

}
