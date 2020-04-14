package cn.qd.order;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDubbo
@ComponentScan("com.cas.common")
@ComponentScan("cn.qd.order")
public class OrderWebApp {
        public static void main(String[] args) {
            SpringApplication.run(OrderWebApp.class, args);
        }
}
