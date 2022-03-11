package org.qrl.mq.rabbitmq.durable.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.qrl.mq.util.RabbitMqTool;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author qr
 * @date 2022/3/10 15:27
 */
public class Producer {

    public static void produce() {
        try {
            Channel channel = RabbitMqTool.getChannel();
            // durable: true 设置队列持久化
            channel.queueDeclare(RabbitMqTool.DEFAULT_QUEUE, true, false, false, null);
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String msg = scanner.next();
                // 发送的消息需要是二进制的
                // props 参数设置消息持久化
                channel.basicPublish("", RabbitMqTool.DEFAULT_QUEUE, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
