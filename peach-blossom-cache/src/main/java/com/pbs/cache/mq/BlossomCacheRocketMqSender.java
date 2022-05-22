package com.pbs.cache.mq;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Objects;

/**
 * @author taoruanliang
 * @date 2022/2/21 21:58
 */
@Slf4j
public class BlossomCacheRocketMqSender {

    @Value("${rocketmq.pgCacheTopic:topic_zd-component-cache}")
    private String topic;

    @Value("${rocketmq.producer.pgCache:pid_zd-component-cache}")
    private String pid;

    @Value("${rocketmq.pgCacheServer:pg}")
    private String pgCacheServer;

    private DefaultMQProducer producer;

    @PostConstruct
    public void init() throws MQClientException {

        Assert.hasText(pgCacheServer, "[rocketmq.name-server] must not be null");

        producer = new DefaultMQProducer(pid, true);
        producer.setInstanceName("pgCache:" + UtilAll.getPid());
        producer.setNamesrvAddr(pgCacheServer);
        producer.setSendMsgTimeout(6000);
        producer.setRetryTimesWhenSendFailed(2);
        producer.setRetryTimesWhenSendAsyncFailed(2);
        producer.setMaxMessageSize(1024 * 1024 * 4);
        producer.setCompressMsgBodyOverHowmuch(1024 * 4);
        producer.setRetryAnotherBrokerWhenNotStoreOK(true);
        producer.start();

        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.start();
    }

    @PreDestroy
    public void destroy() {
        producer.shutdown();
    }

    /**
     * 同步发送
     *
     * @return {@link SendResult}
     */
    public SendResult syncSend(Object obj) {
        try {
            if (Objects.isNull(producer)) {
                //当前不存在配置
                log.warn("当前环境不存在配置，所以不会同步用户信息，请查看,topic:{}, message:{} ", topic, JSON.toJSONString(obj));
                return null;
            }
            //tga为当前应用名
            Message rocketMsg = convertToRocketMQMessage(topic, obj);
            SendResult sendResult = producer.send(rocketMsg, 3000L);
            return sendResult;
        } catch (Exception e) {
            log.error("发送失败， destination:{}, message:{} ,ex:{}", topic, JSON.toJSONString(obj), Throwables.getStackTraceAsString(e));
        }
        return null;
    }

    public Message convertToRocketMQMessage(String destination, Object payloads) {
        String[] tempArr = destination.split(":", 2);
        String topic = tempArr[0];
        String tags = "";
        if (tempArr.length > 1) {
            tags = tempArr[1];
        }
        Message rocketMsg = new Message(topic, tags, JSON.toJSONBytes(payloads));
        return rocketMsg;
    }

}
