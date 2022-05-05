package org.qrl.mq.rabbitmq.durable.producer;

import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.qrl.mq.util.RabbitMqTool;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

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
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    String randomString = RandomUtil.randomString(10);
                    try {
                        // 发布消息时设置属性 MessageProperties.PERSISTENT_TEXT_PLAIN 表示发布的消息持久化
                        channel.basicPublish("", RabbitMqTool.DEFAULT_QUEUE, MessageProperties.PERSISTENT_TEXT_PLAIN, randomString.getBytes(StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        System.out.println("发送消息错误");
                        e.printStackTrace();
                    }
                }
            }, 500, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
