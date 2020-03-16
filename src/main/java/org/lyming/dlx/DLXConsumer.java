package org.lyming.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName DLXConsumer
 * @Description TODO
 * @Author lyming
 * @Date 2020/3/15 10:11 下午
 **/
public class DLXConsumer {

    private final static String EXCHANGE_NAME = "DLX_EXCHANGE";
    private final static String QUEUE_NAME = "DLX_QUEUE";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.2.204");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("admin");
        factory.setPassword("123456");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", EXCHANGE_NAME);
//        arguments.put("x-message-ttl", 10000);
        channel.queueDeclare("TEST_DLX_QUEUE", false, false, false, arguments);
        channel.exchangeDeclare(EXCHANGE_NAME, "topic", false, false, null);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "#");
        System.out.println(" Waiting for message....");

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("Receive Msg: "+msg);
                System.out.println("consumer tag:"+consumerTag);
                System.out.println("DeliveryTag:" + envelope.getDeliveryTag());
            }
        };

        // 开始获取消息
//         String queue, boolean autoAck,Consumer callback
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
