package qr.program.mq.rabbitmq.exchange.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.qrl.mq.util.RabbitMqTool;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.qrl.mq.util.RabbitMqTool.Topic.*;

/**
 * @author qr
 * @date 2022/3/24 20:54
 */
public class Produce {

    public static void main(String[] args) {
        Produce.send();
    }

    @SneakyThrows
    public static void send() {
        try (Channel channel = RabbitMqTool.getChannel ()) {
            channel.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC);
            // 创建多个bindingKey
            Map<String, String> bindingKeyMap = new HashMap<>();
            bindingKeyMap.put(key1,"你是路飞, 你是吃了人人果实幻兽系的太阳神");
            bindingKeyMap.put(key2,"你是罗杰, 你是海贼王");
            bindingKeyMap.put(key3,"你是罗, 你是吃了手术果实的挂逼");
            while (true) {
                for (Map.Entry<String, String> bindingKeyEntry: bindingKeyMap.entrySet()){
                    String bindingKey = bindingKeyEntry.getKey();
                    String message = bindingKeyEntry.getValue();
                    channel.basicPublish(exchange, bindingKey, null, message.getBytes(StandardCharsets.UTF_8));
                    System.out.println("ConsumeMain send: " + message);
                }
            }

        }
    }
}
