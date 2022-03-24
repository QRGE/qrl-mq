package qr.program.mq.rabbitmq.exchange.fanout.exchange;

import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.qrl.mq.util.RabbitMqTool;

/**
 * @author qr
 * @date 2022/3/19 17:06
 */
public class Exchange {

    @SneakyThrows
    public static void createExchange() {
        Channel channel = RabbitMqTool.getChannel();
        // 创建一个 fanout (发布/订阅)类型的交换机
        channel.exchangeDeclare(RabbitMqTool.EXCHANGE_LOG, RabbitMqTool.ExchangeMode.fanout.name());
        System.out.printf("交换机: %s 创建完成%n", RabbitMqTool.EXCHANGE_LOG);
    }
}
