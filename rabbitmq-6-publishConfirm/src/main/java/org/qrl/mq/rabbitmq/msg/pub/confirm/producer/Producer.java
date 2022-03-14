package org.qrl.mq.rabbitmq.msg.pub.confirm.producer;

import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;
import org.qrl.mq.util.RabbitMqTool;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

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

    private static int CONFIRM_COUNT;

    private final static Object CONFIRM_COUNT_LOCK = new Object();

    private static void addConfirmCount() {
        synchronized (CONFIRM_COUNT_LOCK) {
            CONFIRM_COUNT++;
        }
    }

    private static int UN_CONFIRM_COUNT;

    private final static Object UN_CONFIRM_COUNT_LOCK = new Object();

    private static void addUnConfirmCount() {
        synchronized (UN_CONFIRM_COUNT_LOCK) {
            UN_CONFIRM_COUNT++;
        }
    }

    /**
     * 异步确认消息：
     * - 通过设置回调函数，当消息成功接收时调用
     * - 性价比最高，可以及时获取到是哪条消息失败
     */
    public static void msgConfirmAsynch() {
        try {
            int confirmCount = 0;
            int unConfirmCount = 0;
            Channel channel = RabbitMqTool.getChannel();
            // 开启发布确认
            channel.confirmSelect();
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName, true, false, false, null);
            // 消息确认成功回调函数
            ConfirmCallback ackConfirmCallBack = (deliveryTag, multiple) -> addConfirmCount();
            // 消息确认失败回调函数
            ConfirmCallback nackConfirmCallBack = (deliveryTag, multiple) -> addUnConfirmCount();
            channel.addConfirmListener(ackConfirmCallBack, nackConfirmCallBack);
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < MSG_COUNT; i++) {
                String randomString = RandomUtil.randomString(10);
                channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, randomString.getBytes(StandardCharsets.UTF_8));
            }
            long endTime = System.currentTimeMillis();
            System.out.printf("异步确认模式下发送%d条消息需要的时间: %dms, 确认消息条数: %d, 丢失消息条数: %d %n", MSG_COUNT, endTime - startTime, confirmCount, unConfirmCount);
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
