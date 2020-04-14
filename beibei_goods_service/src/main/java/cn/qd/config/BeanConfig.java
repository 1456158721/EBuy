package cn.qd.config;

import com.qd.util.IdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public IdWorker getIdWorker(){
        return new IdWorker(1, 1);
    }
}
