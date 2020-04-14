package cn.qd.search;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.alibaba.dubbo.container.Main;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = "cn.qd.search.dao")
public class SearchServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(SearchServiceApp.class, args);
        Main.main(args);
    }

}
