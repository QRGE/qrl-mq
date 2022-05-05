package org.qrl.mq.rabbitmq.durable.consumer;

import cn.hutool.core.thread.ThreadUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.qrl.mq.util.RabbitMqTool;

/**
 * @author qr
 * @date 2022/3/10 15:06
 */
public class Consumer {

    public static void work(Integer thread) {
        try {
            Channel channel = RabbitMqTool.getChannel ();
            DeliverCallback deliverCallback=(consumerTag, delivery)->{
                String receivedMessage = new String(delivery.getBody());
                System.out.println("线程: " + thread + " 接收到消息: " + receivedMessage);
                ThreadUtil.safeSleep(thread * 3000);
                // deliveryTag: 返回 delivery.getEnvelope().getDeliveryTag()
                // multiple: true 批量处理该信道的应答, 即会批量回答该 channel 上还没应答的消息
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };
            CancelCallback cancelCallback=(consumerTag)-> {
                System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
            };
            System.out.println("消费者: " + thread +" 启动等待消费 ......");
            // 设置自动应答为 false, 进行手动应答
            channel.basicConsume(RabbitMqTool.DEFAULT_QUEUE, false, deliverCallback, cancelCallback);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
