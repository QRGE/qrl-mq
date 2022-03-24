package qr.program.mq.rabbitmq.exchange.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.qrl.mq.util.RabbitMqTool;

import java.nio.charset.StandardCharsets;

import static org.qrl.mq.util.RabbitMqTool.Topic.*;

/**
 * @author qr
 * @date 2022/3/24 20:42
 */
@AllArgsConstructor
public class Consumer {

    public static void main(String[] args) {
        new Consumer(queue1).receive(key1);
        new Consumer(queue2).receive(key2);
        new Consumer(queue3).receive(key3);
        new Consumer(queue4).receive(key4);
    }

    private String name;

    @SneakyThrows
    public void receive(String roundingKey) {
        Channel channel = RabbitMqTool.getChannel ();
        channel.queueDeclare(name, false, false, true, null);
        channel.queueBind(name, exchange, roundingKey);
        System.out.println("对列: " + name + ", 等待接收消息 .....");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(name + " 接收消息: " + message);
        };
        channel.basicConsume(name, true, deliverCallback, consumerTag -> { });
    }
}
