package org.qrl.mq.rabbitmq.strategy;


import cn.hutool.core.thread.ThreadUtil;
import org.qrl.mq.rabbitmq.strategy.consumer.Consumer;
import org.qrl.mq.rabbitmq.strategy.producer.Producer;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 对比一下公平消费和非公平消费的消息个数
 * @author qr
 * @date 2022/3/10 15:06
 */
public class MsgSendStrategyMain {

    public static void main(String[] args) {
        ScheduledThreadPoolExecutor executor = ThreadUtil.createScheduledExecutor(10);
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            executor.execute(() -> new Consumer().work(finalI, finalI+1));
        }
        Producer.produce();
    }
}
