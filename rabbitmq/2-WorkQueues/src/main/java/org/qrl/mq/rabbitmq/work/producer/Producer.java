package org.qrl.mq.rabbitmq.work.producer;

import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.Channel;
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
            channel.queueDeclare(RabbitMqTool.DEFAULT_QUEUE, false, false, false, null);
            int count = 10000;
            while (count-- > 0) {
                String msg = RandomUtil.randomString(10);
                // 发送的消息需要是二进制的
                channel.basicPublish("", RabbitMqTool.DEFAULT_QUEUE, null, msg.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
