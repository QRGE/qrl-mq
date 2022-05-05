package org.qrl.mq.rabbitmq.work;

import cn.hutool.core.thread.ThreadUtil;
import org.qrl.mq.rabbitmq.work.consumer.Worker;
import org.qrl.mq.rabbitmq.work.producer.Producer;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * rabbitmq 默认的发布消息策略就是轮训
 * 默认情况下
 * @author qr
 * @date 2022/3/10 15:06
 */
public class Main {

    public static void main(String[] args) {
        ThreadUtil.execAsync(Producer::produce);
        ScheduledThreadPoolExecutor executor = ThreadUtil.createScheduledExecutor(10);
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            executor.execute(() -> new Worker().work(finalI));
        }
    }
}
