/**
 * 死信：无法被消费的消息，一般来说，producer 将消息投递到 broker 或者直接到 queue 里了，
 * consumer 从 queue 取出消息进行消费，但某些时候由于"特定的原因导致 queue 中的某些消息无法被消费"，
 * 这样的消息如果没有后续的处理，就变成了死信，有死信自然就有了死信队列。
 * 业务场景：用户在商城下单成功并点击去支付后在指定时间未支付时自动失效
 * 死信来源：
 * - 消息 TTL 过期 (设置expiration)
 * - 队列达到最大长度(队列满了，无法再添加数据到 mq 中) (设置x-max-length)
 * - 消息被拒绝(basic.reject 或 basic.nack) 且 requeue=false.
 *
 */
package org.qrl.mq.rabbitmq.deadQueue;
