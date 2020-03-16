package org.lyming.first;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.security.Key;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName RabbitMQProducer
 * @Description TODO
 * @Author lyming
 * @Date 2020/3/14 12:17 上午
 **/
public class RabbitMQProducer {

    private final static String EXCHANGE_NAME = "SIMPLE_EXCHANGE";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.2.204");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("admin");
        factory.setPassword("123456");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String msg = "Hello World:rabbit MQ";
        for (int i = 0; i < 1000; i++) {
            channel.basicPublish(EXCHANGE_NAME, "key1", null, msg.getBytes());
        }
        channel.close();
        connection.close();
    }
}
