package org.qrl.mq.rabbtmq.start.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;
import org.qrl.mq.rabbtmq.start.config.RabbitmqConfig;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产端
 * @author qr
 * @date 2022/3/9 23:33
 */
public class Producer {

    @SneakyThrows
    public void produce(String queueName, RabbitmqConfig config) {
        // 创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(config.getIp());
        factory.setUsername(config.getUsername());
        factory.setPassword(config.getPassword());
        factory.setVirtualHost("/");
        // 创建队列
        Connection connection = factory.newConnection();
        // 获取信道, 简单的连接 broker 可以不显示制定 exchange (使用默认的)
        Channel channel = connection.createChannel();
        /*
         * queueDeclare 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化
         * 3.该队列是否进行消息共享(排他) 是否进行共享 true 可以多个消费者消费
         * 4.是否自动删除
         * - 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
         * 5.其他参数: 例如延时消息, 死信消息
         * */
        channel.queueDeclare(queueName,false,false,false,null);
        String message="你好哇";
        /*
         * basicPublish() 发送一个消息
         * 1.发送到那个交换机(空的即默认)
         * 2.路由的 key 是哪个
         * 3.其他的参数信息
         * 4.发送消息的消息体
         * */
        channel.basicPublish("",queueName,null, message.getBytes());
        System.out.println("消息发送完毕");
    }
}
