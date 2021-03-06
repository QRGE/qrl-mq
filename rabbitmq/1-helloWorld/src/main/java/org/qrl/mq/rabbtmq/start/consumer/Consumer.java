package org.qrl.mq.rabbtmq.start.consumer;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;
import org.qrl.mq.rabbtmq.start.config.RabbitmqConfig;

/**
 * @author qr
 * @date 2022/3/10 14:09
 */
public class Consumer {

    @SneakyThrows
    public void consume(String queueName, RabbitmqConfig config) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(config.getIp());
        factory.setUsername(config.getUsername());
        factory.setPassword(config.getPassword());
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        System.out.println("等待接收消息 ....");
        // 消费接口回调
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println(consumerTag);
            String deliverMsg = new String(message.getBody());
            System.out.println("消息体: " + deliverMsg);
        };
        // 取消消费的回调
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println(consumerTag);
            System.out.println("消息消费被中断/取消");
        };
        /*
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.消费者取消消费的回调
         * */
        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }
}
