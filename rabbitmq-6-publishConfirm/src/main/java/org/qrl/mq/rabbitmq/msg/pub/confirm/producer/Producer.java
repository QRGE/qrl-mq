package org.qrl.mq.rabbitmq.msg.pub.confirm.producer;

import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;
import org.qrl.mq.util.RabbitMqTool;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author qr
 * @date 2022/3/10 15:27
 */
@SuppressWarnings("unused")
public class Producer {

    private final static int MSG_COUNT = 1000;

    /**
     * 同步确认消息，即发送的消息需要确认后才会发布下一条消息
     * - 发布消息速率比较慢，但比较安全
     */
    public static void msgConfirmSynch() {
        try {
            Channel channel = RabbitMqTool.getChannel();
            // 开启发布确认
            channel.confirmSelect();
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName, true, false, false, null);
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < MSG_COUNT; i++) {
                String randomString = "消息: " + (i+1) + ", " +RandomUtil.randomString(10);
                channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, randomString.getBytes(StandardCharsets.UTF_8));
                // 发布确认
                boolean confirms = channel.waitForConfirms();
                if (!confirms) {
                    System.out.println("消息同步失败!");
                    return;
                }
            }
            long endTime = System.currentTimeMillis();
            System.out.printf("同步确认模式下发送%d条消息需要的时间: %dms%n", MSG_COUNT, endTime - startTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步确认消息：
     * - 通过设置回调函数，当消息成功接收时调用
     * - 性价比最高，可以及时获取到是哪条消息失败
     */
    public static void msgConfirmAsynch() {
        try {
            Channel channel = RabbitMqTool.getChannel();
            // 开启发布确认
            channel.confirmSelect();
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName, true, false, false, null);
            // 一个线程安全，支持高并发的哈希表, 用于存储消息及在消息确认的回调函数中传递
            ConcurrentSkipListMap<Long, String> msgMap = new ConcurrentSkipListMap<>();
            // 消息确认成功回调函数
            ConfirmCallback ackConfirmCallBack = (deliveryTag, multiple) -> {
                // 批量模式下获取某个 tag 的所有 k : values
                if (multiple) {
                    // 删除成功发布的消息
                    ConcurrentNavigableMap<Long, String> map = msgMap.headMap(deliveryTag);
                    map.clear();
                }else { // 非批量的情况
                    msgMap.remove(deliveryTag);
                }
            };
            // 消息确认失败回调函数(主要在处理失败消息处)
            // 可以把未确认的消息放到一个基于内存的能被发布线程访问的队列， 比如说用 ConcurrentLinkedQueue 这个队列在 confirm callbacks 与发布线程之间进行消息的传递。
            ConfirmCallback nackConfirmCallBack = (deliveryTag, multiple) -> {
                String msg = msgMap.get(deliveryTag);
                System.out.println("未发布确认的消息: " + msg);
            };
            channel.addConfirmListener(ackConfirmCallBack, nackConfirmCallBack);
            long startTime = System.currentTimeMillis();
            for (int i = 1; i < MSG_COUNT+1; i++) {
                String randomString = RandomUtil.randomString(10);
                // getNextPublishSeqNo() 用于调取下一次发布的消息的序号
                msgMap.put(channel.getNextPublishSeqNo(), randomString);
                channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, randomString.getBytes(StandardCharsets.UTF_8));
            }
            long endTime = System.currentTimeMillis();
            System.out.printf("异步确认模式下发送%d条消息需要的时间: %dms%n", MSG_COUNT, endTime - startTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量确认消息
     * - 出问题时无法确认时哪条消息丢失
     */
    public static void msgConfirmBatch() {
        try {
            Channel channel = RabbitMqTool.getChannel();
            // 开启发布确认
            channel.confirmSelect();
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName, true, false, false, null);
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < MSG_COUNT; i++) {
                String randomString = RandomUtil.randomString(10);
                channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, randomString.getBytes(StandardCharsets.UTF_8));
            }
            // 即当消息发布一定数量后再进行 waitForConfirms()
            boolean confirms = channel.waitForConfirms();
            if (!confirms) {
                System.out.println("消息同步失败!");
                return;
            }
            long endTime = System.currentTimeMillis();
            System.out.printf("批量确认模式下发送%d条消息需要的时间: %dms%n", MSG_COUNT, endTime - startTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
