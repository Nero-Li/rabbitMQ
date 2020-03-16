package org.lyming.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName DLXProducer
 * @Description TODO
 * @Author lyming
 * @Date 2020/3/16 10:16 上午
 **/
public class DLXProducer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.2.204");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("admin");
        factory.setPassword("123456");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String msg = "Hello World:rabbit DLX";
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .expiration("10000")
                .build();
        for (int i = 0; i < 1000; i++) {
            channel.basicPublish("", "TEST_DLX_QUEUE", properties, msg.getBytes());
//            channel.basicPublish("", "TEST_DLX_QUEUE", null, msg.getBytes());
        }
        channel.close();
        connection.close();
    }
}
