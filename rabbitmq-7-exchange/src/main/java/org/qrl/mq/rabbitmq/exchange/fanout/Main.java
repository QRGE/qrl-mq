package org.qrl.mq.rabbitmq.exchange.fanout;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.Callable;

/**
 * @author qr
 * @date 2022/3/19 16:41
 */
public class Main {

    public static void main(String[] args) {
        Exchange.createExchange();
        new Producer("生产者1号").emitLog(10);
        new Consumer("交换机1号").receiveLog();
        new Consumer("交换机2号").receiveLog();
    }
}
