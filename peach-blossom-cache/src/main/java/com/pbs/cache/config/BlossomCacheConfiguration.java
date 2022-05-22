package com.pbs.cache.config;

import com.pbs.cache.annotation.BlossomCacheAop;
import com.pbs.cache.annotation.BlossomEvictAop;
import com.pbs.cache.mq.BlossomCacheRocketMqConsumer;
import com.pbs.cache.mq.BlossomCacheRocketMqSender;
import com.pbs.cache.parser.BlossomCacheAnnotationParser;
import com.pbs.cache.parser.BlossomEvictAnnotationParser;
import org.springframework.context.annotation.Bean;

/**
 * @author taoruanliang
 * @date 2022/2/16 14:31
 */
public class BlossomCacheConfiguration {

    @Bean
    public BlossomCacheAop blossomCacheAop() {
        return new BlossomCacheAop();
    }

    @Bean
    public BlossomEvictAop blossomEvictAop() {
        return new BlossomEvictAop();
    }

    @Bean
    public BlossomCacheAnnotationParser blossomCacheAnnotationParser() {
        return new BlossomCacheAnnotationParser();
    }

    @Bean
    public BlossomEvictAnnotationParser blossomEvictAnnotationParser() {
        return new BlossomEvictAnnotationParser();
    }

    @Bean
    public BlossomCacheRocketMqSender blossomCacheRocketMqSender() {
        return new BlossomCacheRocketMqSender();
    }

    @Bean
    public BlossomCacheRocketMqConsumer blossomCacheRocketMqConsumer() {
        return new BlossomCacheRocketMqConsumer();
    }
}
