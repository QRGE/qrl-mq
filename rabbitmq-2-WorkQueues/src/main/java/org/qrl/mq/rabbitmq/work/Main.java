package org.qrl.mq.rabbitmq.work;

import cn.hutool.core.thread.ThreadUtil;
import org.qrl.mq.rabbitmq.work.consumer.Worker;
import org.qrl.mq.rabbitmq.work.producer.Producer;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * TODO 这里并没有说明为什么线程会轮流接收消息
 * 默认情况下
 * @author qr
 * @date 2022/3/10 15:06
 */
public class Main {

    public static void main(String[] args) {
        ScheduledThreadPoolExecutor executor = ThreadUtil.createScheduledExecutor(10);
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            executor.execute(() -> new Worker().work(finalI));
        }
        Producer.produce();
    }
}
