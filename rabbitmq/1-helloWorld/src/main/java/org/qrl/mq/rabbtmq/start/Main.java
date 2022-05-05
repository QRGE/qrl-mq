package org.qrl.mq.rabbtmq.start;

import org.qrl.mq.rabbtmq.start.config.RabbitmqConfig;
import org.qrl.mq.rabbtmq.start.consumer.Consumer;
import org.qrl.mq.rabbtmq.start.producer.Producer;

/**
 * @Author qr
 * @Date 2022/5/5-15:48
 */
public class Main {


    public static void main(String[] args) {
        RabbitmqConfig config = new RabbitmqConfig();
        String queueName = "Hello";
        new Producer().produce(queueName, config);
        new Consumer().consume(queueName, config);
    }
}
