package org.lyming.ttl;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName TTLProducer
 * @Description TODO
 * @Author lyming
 * @Date 2020/3/15 7:30 下午
 **/
public class TTLProducer {

    private final static String QUEUE_NAME = "TEST_TTL_QUEUE";
    private final static String EXCHANGE_NAME = "TEST_TTL_EXCHANGE";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.2.204");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123456");
        factory.setVirtualHost("/");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String msg = "Hello TTL rabbit MQ";
        //单条消息设置10s过期
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .deliveryMode(2)//持久化
                .contentEncoding("UTF-8")
                .expiration("10000")//TTL
                .build();
        Map<String, Object> argss = new HashMap<>();
        //队列设置6s过期  设置为5s启动报错
        argss.put("x-message-ttl", 6000);
//        channel.exchangeDeclare("TEST_TTL_EXCHANGE", "direct", true, false, null);
        channel.queueDeclare("TEST_TTL_QUEUE", false, false, false, argss);
//        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "TEST_TTL_DLX");
        channel.basicPublish(EXCHANGE_NAME,"TEST_TTL_DLX",properties,msg.getBytes());
        channel.close();
        connection.close();
    }
}
