package org.qrl.mq.rabbitmq.deadQueue.ttl;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.qrl.mq.util.RabbitMqTool;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Consumer {

    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMqTool.getChannel ();
        // 声明 死信 和 普通交换机 类型为direct
        channel.exchangeDeclare(RabbitMqTool.DeadQueueDemo.normalExchange, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(RabbitMqTool.DeadQueueDemo.deadExchange, BuiltinExchangeType.DIRECT);
        // 声明死信队列
        String deadQueue = "dead-queue";
        channel.queueDeclare(deadQueue, false, false, false, null);
        // 死信队列绑定死信交换机与routing-key
        channel.queueBind(deadQueue, RabbitMqTool.DeadQueueDemo.deadExchange, "lisi");
        // 正常队列绑定死信队列信息
        Map<String, Object> params = new HashMap<>();
        // 正常队列设置死信交换机
        params.put("x-dead-letter-exchange", RabbitMqTool.DeadQueueDemo.deadExchange);
        // 正常队列设置死信routing-key
        params.put("x-dead-letter-routing-key", "lisi");
        // 声明正常队列
        String normalQueue = "normal-queue";
        // 正常队列绑定死信队列
        channel.queueDeclare(normalQueue, false, false, false, params);
        channel.queueBind(normalQueue, RabbitMqTool.DeadQueueDemo.normalExchange, "zhangsan");
        System.out.println("等待接收消息 .....");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Consumer 接收到消息" + message);
        };
        channel.basicConsume(normalQueue, true, deliverCallback, consumerTag -> {});
    }
}