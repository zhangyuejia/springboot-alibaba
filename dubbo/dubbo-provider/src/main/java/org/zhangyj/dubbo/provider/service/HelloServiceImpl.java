package org.zhangyj.dubbo.provider.service;

import org.apache.dubbo.config.annotation.Service;
import org.zhangyj.dubbo.dubboapi.service.IHelloService;

/**
 * @author ZHANG-YJ
 */
@Service
public class HelloServiceImpl implements IHelloService {

    public String sayHello(String name) {
        return "Hello " + name + ". I am zhangyj.";
    }
}
