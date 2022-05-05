package org.qrl.mq.rabbitmq.strategy.consumer;

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

    private int count = 0;

    public void work(Integer thread, int prefetchCount) {
        try {
            Channel channel = RabbitMqTool.getChannel ();
            DeliverCallback deliverCallback=(consumerTag, delivery)->{
                String receivedMessage = new String(delivery.getBody());
                plusCount();
                System.out.println("线程: " + thread + " 接收到消息: " + receivedMessage + " 消费消息个数: " + count);
                ThreadUtil.safeSleep(thread * 50000);
                // 手动回答消息
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };
            CancelCallback cancelCallback=(consumerTag)-> {
                System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
            };
            System.out.println("消费者: " + thread +" 启动等待消费 ......");
            // prefetchCount = 1代表不公平分发, 2 及以上设置预取值的个数
            // 预取值定义通道上允许的未确认消息的最大数量
            // Qos: quality of service / 预取值，设置消费者获取消息的个数
            channel.basicQos(prefetchCount);
            //  设置自动应答为 false, 进行手动应答
            channel.basicConsume(RabbitMqTool.DEFAULT_QUEUE, false, deliverCallback, cancelCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void plusCount() {
        synchronized (this) {
            count++;
        }
    }
}
