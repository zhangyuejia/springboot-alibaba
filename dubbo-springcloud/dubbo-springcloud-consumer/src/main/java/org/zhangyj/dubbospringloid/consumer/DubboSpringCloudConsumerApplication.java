package org.zhangyj.dubbospringloid.consumer;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ZHANG-YJ
 */
@DubboComponentScan
@SpringBootApplication
public class DubboSpringCloudConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboSpringCloudConsumerApplication.class, args);
    }
}
