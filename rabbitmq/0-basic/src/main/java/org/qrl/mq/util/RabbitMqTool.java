package org.qrl.mq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author qr
 * @date 2022/3/10 14:51
 */
public class RabbitMqTool {

    public final static String DEFAULT_QUEUE = "Hello";

    public final static String EXCHANGE_LOG = "Log";

    public final static String LOG_ROUTING_KEY = "log_link";

    public interface DirectDemo {
        String exchange = "Direct_Log";
        String queue1 = "queue1";
        String queue2 = "queue2";
        String green = "green";
        String black = "black";
        String yellow = "yellow";
    }

    /**
     * 死信队列的一些配置
     */
    public interface DeadQueueDemo {
        String normalExchange = "normal_exchange";
        String deadExchange = "dead_exchange";
        String roundingKeyNormal = "zhangsan";
        String roundingKeyDeadLetter = "lisi";
        String deadQueue = "dead-queue";
        String normalQueue = "normal-queue";
    }

    public interface Topic {
        String exchange = "Topic";
        String queue1 = "queue1";
        String queue2 = "queue2";
        String queue3 = "queue3";
        String queue4 = "queue4";
        String key1 = "Munch.d.Luffy";
        String key2 = "Gore.D.Roger";
        String key3 = "Trafalgar.D.Rowe";
        String key4 = "*.D.*";
    }

    /**
     * 初始化 ConnectionFactory
     */
    private final static ConnectionFactory factory = new ConnectionFactory();
    static {
        factory.setHost("1.12.221.233");
        factory.setUsername("qr");
        factory.setPassword("QRWUDI666");
    }

    /**
     * 建立信道 channel
     * @return channel
     * @throws Exception 反正就是连接失败了。。。
     */
    public static Channel getChannel() throws Exception {
        Connection connection = factory.newConnection();
        return connection.createChannel();
    }

    @AllArgsConstructor
    @Getter
    public enum ExchangeMode {
        // 发布订阅模式
        fanout;
    }
}
