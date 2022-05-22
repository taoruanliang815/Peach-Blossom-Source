package com.pbs.cache.mq;

import com.alibaba.fastjson.JSON;
import com.pbs.cache.event.Event;
import com.pbs.cache.event.Publisher;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.InitializingBean;

import java.nio.charset.StandardCharsets;

/**
 * @author taoruanliang
 * @date 2022/3/10 17:08
 */
@Slf4j
@RocketMQMessageListener(
        messageModel = MessageModel.BROADCASTING,
        topic = "${rocketmq.pgCacheTopic:topic_blossom-cache}",
        consumerGroup = "${rocketmq.consumer.blossomGroup}")
public class BlossomCacheRocketMqConsumer implements RocketMQListener<MessageExt>, InitializingBean {

    @Override
    public void onMessage(MessageExt message) {

        String msgStr = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("[PointGoldCacheRocketMqConsumer] start:{}", msgStr);

        try {
            BlossomCacheMessage blossomCacheMessage = JSON.parseObject(msgStr, BlossomCacheMessage.class);

            Publisher.publish(Event.event(blossomCacheMessage));
        } catch (Exception e) {
            log.error("PointGoldCacheRocketMqConsumer error:{}", e.fillInStackTrace());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Publisher.subscribe();
    }
}
