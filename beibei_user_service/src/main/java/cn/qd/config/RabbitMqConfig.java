package cn.qd.config;


import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${beibei.mq.queue}")
    private String queueName;

    @Bean
    public Queue createQueue(){
        return new Queue(queueName);
    }
}
