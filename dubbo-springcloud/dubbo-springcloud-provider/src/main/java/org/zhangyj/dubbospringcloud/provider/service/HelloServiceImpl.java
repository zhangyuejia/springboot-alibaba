package org.zhangyj.dubbospringcloud.provider.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.zhangyj.dubbospringcloud.providerapi.service.IHelloService;

/**
 * @author ZHANG-YJ
 */
@DubboService
public class HelloServiceImpl implements IHelloService {

    public String sayHello(String name) {
        return "Hello " + name + ". I am zhangyj.";
    }
}
