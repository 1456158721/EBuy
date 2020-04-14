package cn.test;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TestConsumer {

    public static void main(String[] args) {

        //新建 一个 连接 工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");

        Connection connection = null;

        try {
            connection = connectionFactory.newConnection();

            Channel channel = connection.createChannel();
            channel.queueDeclare("test2", true, false, false, null);
            //消费方法
            DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    String str = new String(body);
                    System.out.println("接收的消息: " + str);
                }
            };
            //设置 监听
            channel.basicConsume("test2", true, defaultConsumer);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


    }
}
