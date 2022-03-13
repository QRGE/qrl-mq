package org.qrl.mq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author qr
 * @date 2022/3/10 14:51
 */
public class RabbitMqTool {

    public final static String DEFAULT_QUEUE = "Hello";

    /**
     * 初始化 ConnectionFactory
     */
    private final static ConnectionFactory factory = new ConnectionFactory();
    static {
        factory.setHost("120.77.16.74");
        factory.setUsername("admin");
        factory.setPassword("admin");
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
}