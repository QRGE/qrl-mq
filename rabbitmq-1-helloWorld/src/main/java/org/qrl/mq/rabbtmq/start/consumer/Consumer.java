package org.qrl.mq.rabbtmq.start.consumer;

import com.rabbitmq.client.*;

/**
 * @author qr
 * @date 2022/3/10 14:09
 */
public class Consumer {

    private final static String QUEUE_NAME = "Hello";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("1.12.221.233");
        factory.setUsername("qr");
        factory.setPassword("QRWUDI666");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        System.out.println("等待接收消息 ....");
        // 消费接口回调
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println(consumerTag);
            String deliverMsg = new String(message.getBody());
            System.out.println("消息体: " + deliverMsg);
        };
        // 取消消费的回调
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println(consumerTag);
            System.out.println("消息消费被中断/取消");
        };
        /*
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.消费者取消消费的回调
         * */
        channel.basicConsume(QUEUE_NAME,true, deliverCallback, cancelCallback);
    }
}
