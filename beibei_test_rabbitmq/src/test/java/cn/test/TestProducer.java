package cn.test;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TestProducer {

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
            //创建 信息
            Channel channel = connection.createChannel();

            //声明一个 队列
            /**
             * queue 队列的名称
             * durable  是否持久化
             * exclusive 独占连接
             * autoDelete 自动删除
             * arguments  附加参数
             */
            channel.queueDeclare("test2", true, false, false, null);

            //发送 消息
            channel.basicPublish("", "test2", null, "发送的消息".getBytes());

            System.out.println("发送成功");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
