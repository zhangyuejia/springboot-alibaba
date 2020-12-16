package org.zhangyj.dubbospringloid.consumer.mock.service;

import org.zhangyj.dubbospringcloud.providerapi.service.IHelloService;

/**
 * HelloService服务降级
 * @author ZHANG-YJ
 */
public class MockHelloServiceImpl implements IHelloService {

    public String sayHello(String name) {
        return "对不起，访问的人太多了，请稍后再试。";
    }
}
