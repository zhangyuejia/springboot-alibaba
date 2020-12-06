package ory.zhangyj.dubbo.consumer;

import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.zhangyj.dubbo.dubboapi.service.IHelloService;

/**
 * @author ZHANG-YJ
 */
@DubboComponentScan
@SpringBootApplication
public class DubboConsumerApplication {

    @Reference
    private IHelloService helloService;

    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerApplication.class, args);
    }

    @Bean
    public ApplicationRunner runner(){
        return new ApplicationRunner() {
            public void run(ApplicationArguments args) throws Exception {
                System.out.println(helloService.sayHello("provider"));
            }
        };
    }
}
