package org.qrl.mq.rabbitmq.deadQueue;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.qrl.mq.util.RabbitMqTool;

import java.nio.charset.StandardCharsets;

import static org.qrl.mq.util.RabbitMqTool.DeadQueueDemo.*;

public class ConsumerDeadLetter {


    /**
     * 绑定的死信队列的消费者, 用于处理死信队列中的消息
     */
    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMqTool.getChannel ();
        channel.exchangeDeclare(deadExchange, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(deadQueue, false, false, false, null);
        channel.queueBind(deadQueue, deadExchange, roundingKeyDeadLetter);
        System.out.println("等待接收死信队列消息 .....");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("ConsumerDeadLetter 接收死信队列的消息" + message);
        };
        channel.basicConsume(deadQueue, true, deliverCallback, consumerTag -> {});
    }
}