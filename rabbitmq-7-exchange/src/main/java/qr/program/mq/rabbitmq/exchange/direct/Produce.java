package qr.program.mq.rabbitmq.exchange.direct;

import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import lombok.SneakyThrows;
import org.qrl.mq.util.RabbitMqTool;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static org.qrl.mq.util.RabbitMqTool.DirectDemo.*;

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
            channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT);
            //创建多个bindingKey
            Map<String, String> bindingKeyMap = new HashMap<>();
            bindingKeyMap.put(green,"绿色消息");
            bindingKeyMap.put(black,"黑色消息");
            bindingKeyMap.put(yellow,"黄色消息");
            //debug 没有消费这接收这个消息 所有就丢失了
            bindingKeyMap.put("blue","蓝色消息");
            for (Map.Entry<String, String> bindingKeyEntry: bindingKeyMap.entrySet()){
                String bindingKey = bindingKeyEntry.getKey();
                String message = bindingKeyEntry.getValue();
                channel.basicPublish(exchange, bindingKey, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("ConsumeMain send: " + message);
            }

        }
    }
}
