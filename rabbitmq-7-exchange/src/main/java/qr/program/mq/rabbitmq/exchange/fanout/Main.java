package qr.program.mq.rabbitmq.exchange.fanout;

import cn.hutool.core.thread.ThreadUtil;
import qr.program.mq.rabbitmq.exchange.fanout.consumer.Consumer;
import qr.program.mq.rabbitmq.exchange.fanout.producer.Producer;

import java.util.concurrent.Callable;

/**
 * @author qr
 * @date 2022/3/19 16:41
 */
public class Main {

    public static void main(String[] args) {
        ThreadUtil.execAsync(() -> new Producer("生产者1号").emitLog(10000));
        ThreadUtil.execAsync(() -> new Consumer().receiveLog());
        ThreadUtil.execAsync(() -> new Consumer().receiveLog());
    }
}
