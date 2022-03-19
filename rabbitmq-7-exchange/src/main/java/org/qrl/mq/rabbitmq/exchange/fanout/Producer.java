package org.qrl.mq.rabbitmq.exchange.fanout;

import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.qrl.mq.util.RabbitMqTool;

import java.nio.charset.StandardCharsets;

/**
 * @author qr
 * @date 2022/3/19 16:35
 */
@Getter
@Setter
@AllArgsConstructor
public class Producer {

    private String name;

    @SneakyThrows
    public void emitLog(Integer num) {
        String exchange = RabbitMqTool.EXCHANGE_LOG;
        Channel channel = RabbitMqTool.getChannel();
        System.out.printf("生产者: %s 正在工作。。。%n", name);
        // 发布给指定 boundingKey 的对列
        for (int i = 0; i < num; i++) {
            // 发布10位的随机字符
            String content = RandomUtil.randomString(10);
            channel.basicPublish(exchange, "", null, content.getBytes(StandardCharsets.UTF_8));
        }
    }
}
