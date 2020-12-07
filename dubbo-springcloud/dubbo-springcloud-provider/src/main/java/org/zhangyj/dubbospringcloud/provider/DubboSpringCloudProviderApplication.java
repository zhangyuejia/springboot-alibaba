package org.zhangyj.dubbospringcloud.provider;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ZHANG-YJ
 */
@DubboComponentScan
@SpringBootApplication
public class DubboSpringCloudProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboSpringCloudProviderApplication.class, args);
    }
}
