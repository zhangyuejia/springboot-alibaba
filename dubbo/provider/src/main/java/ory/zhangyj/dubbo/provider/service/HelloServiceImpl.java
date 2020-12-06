package ory.zhangyj.dubbo.provider.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.zhangyj.dubbo.dubboapi.service.IHelloService;

/**
 * @author ZHANG-YJ
 */
@DubboService
public class HelloServiceImpl implements IHelloService {

    public String sayHello(String name) {
        return "Hello " + name + ". I am zhangyj.";
    }
}
