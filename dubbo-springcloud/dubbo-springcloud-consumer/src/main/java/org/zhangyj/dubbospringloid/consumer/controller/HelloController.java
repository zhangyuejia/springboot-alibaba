package org.zhangyj.dubbospringloid.consumer.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zhangyj.dubbospringcloud.providerapi.service.IHelloService;

/**
 * @author ZHANG-YJ
 */
@RestController
public class HelloController {

    @DubboReference
    private IHelloService helloService;

    @RequestMapping("say")
    public String sayHello(String name) {
        return helloService.sayHello(name);
    }
}
