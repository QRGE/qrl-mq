package org.qrl.mq.rabbitmq.msg.pub.confirm;


import org.qrl.mq.rabbitmq.msg.pub.confirm.producer.Producer;

/**
 * 对比一下公平消费和非公平消费的消息个数
 * - 异步确认的性价比最高
 * @author qr
 * @date 2022/3/10 15:06
 */
public class MsgPubConfirmMain {

    public static void main(String[] args) {
        Producer.msgConfirmSynch();
        Producer.msgConfirmBatch();
        Producer.msgConfirmAsynch();
    }
}
