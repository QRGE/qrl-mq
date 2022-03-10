package org.qrl.mq.rabbitmq.work.consumer;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.qrl.mq.util.RabbitMqTool;

/**
 * @author qr
 * @date 2022/3/10 15:06
 */
public class Worker {

    public void work(Integer thread) {
        try {
            Channel channel = RabbitMqTool.getChannel ();
            DeliverCallback deliverCallback=(consumerTag, delivery)->{
                String receivedMessage = new String(delivery.getBody());
                System.out.println("线程: " + thread + " 接收到消息: " + receivedMessage);
            };
            CancelCallback cancelCallback=(consumerTag)-> {
                System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
            };
            System.out.println("消费者: " + thread +" 启动等待消费 ......");
            channel.basicConsume(RabbitMqTool.DEFAULT_QUEUE,true, deliverCallback, cancelCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
