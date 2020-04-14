package cn.qd.user;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDubbo
@ComponentScan("com.cas.common")
@ComponentScan("cn.qd.user")
public class UserWebApp {
        public static void main(String[] args) {
            SpringApplication.run(UserWebApp.class, args);
        }
}
