package ory.zhangyj.dubbo.consumer;

import org.apache.dubbo.config.annotation.DubboReference;
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

    @DubboReference(url = "dubbo://192.168.149.1:20880/org.zhangyj.dubbo.dubboapi.service.IHelloService")
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
