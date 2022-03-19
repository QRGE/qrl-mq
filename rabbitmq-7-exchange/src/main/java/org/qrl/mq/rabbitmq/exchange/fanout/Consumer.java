package org.qrl.mq.rabbitmq.exchange.fanout;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.qrl.mq.util.RabbitMqTool;

import java.nio.charset.StandardCharsets;

/**
 * @author qr
 * @date 2022/3/19 11:44
 */
@AllArgsConstructor
@Getter
@Setter
public class Consumer {

    private String queueName;

    @SneakyThrows
    public void receiveLog() {
        String exchange = RabbitMqTool.EXCHANGE_LOG;
        Channel channel = RabbitMqTool.getChannel();
        // 创建一个默认对列
        String queue = channel.queueDeclare(exchange, true, false, true, null).getQueue();
        System.out.printf("交换机: %s 创建完成%n", queueName);
        // 绑定对列和交换机
        channel.queueBind(queue, exchange, "");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Receive msg: " + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        CancelCallback cancelCallback = consumerTag -> System.out.println("Reject msgId: " + consumerTag);
        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }
}
