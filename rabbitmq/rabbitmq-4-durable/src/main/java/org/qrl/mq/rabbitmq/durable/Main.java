package org.qrl.mq.rabbitmq.durable;


import cn.hutool.core.thread.ThreadUtil;
import org.qrl.mq.rabbitmq.durable.consumer.Consumer;
import org.qrl.mq.rabbitmq.durable.producer.Producer;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author qr
 * @date 2022/3/10 15:06
 */
public class Main {

    public static void main(String[] args) {
        ScheduledThreadPoolExecutor executor = ThreadUtil.createScheduledExecutor(10);
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            executor.execute(() -> Consumer.work(finalI));
        }
        Producer.produce();
    }
}
