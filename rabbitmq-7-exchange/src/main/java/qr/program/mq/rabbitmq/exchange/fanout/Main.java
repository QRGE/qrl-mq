package qr.program.mq.rabbitmq.exchange.fanout;

import qr.program.mq.rabbitmq.exchange.fanout.consumer.Consumer;
import qr.program.mq.rabbitmq.exchange.fanout.exchange.Exchange;
import qr.program.mq.rabbitmq.exchange.fanout.producer.Producer;

/**
 * @author qr
 * @date 2022/3/19 16:41
 */
public class Main {

    public static void main(String[] args) {
        new Producer("生产者1号").emitLog(100);
        new Consumer().receiveLog();
        new Consumer().receiveLog();
    }
}
