package cn.qd;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.alibaba.dubbo.container.Main;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = "cn.qd.dao")
public class GoodServerApp {

    public static void main(String[] args) {
        SpringApplication.run(GoodServerApp.class, args);
        Main.main(args);
    }
}
