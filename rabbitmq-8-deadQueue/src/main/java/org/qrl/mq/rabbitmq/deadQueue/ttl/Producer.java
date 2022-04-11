package org.qrl.mq.rabbitmq.deadQueue.ttl;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.qrl.mq.util.RabbitMqTool;

public class Producer {


    public static void main(String[] argv) throws Exception {
        try (Channel channel = RabbitMqTool.getChannel ()) {
            channel.exchangeDeclare(RabbitMqTool.DeadQueueDemo.normalExchange, BuiltinExchangeType.DIRECT);
            //设置消息的TTL 时间
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
            while (true) {
                ThreadUtil.sleep(300);
                String message = RandomUtil.randomString(10);
                channel.basicPublish(RabbitMqTool.DeadQueueDemo.normalExchange,RabbitMqTool.DeadQueueDemo.roundingKey1, properties, message.getBytes());
                System.out.println("生产者发送消息: " + message);
            }
        }
    }
}