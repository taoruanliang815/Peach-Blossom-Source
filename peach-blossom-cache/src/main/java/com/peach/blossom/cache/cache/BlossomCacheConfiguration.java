package com.peach.blossom.cache.cache;

import com.peach.blossom.cache.aop.BlossomCacheAop;
import com.peach.blossom.cache.parser.BlossomCacheAnnotationParser;
import org.springframework.context.annotation.Bean;

/**
 * @author taoruanliang
 * @date 2022/2/16 14:31
 */
public class BlossomCacheConfiguration {

    @Bean
    public BlossomCacheAop blossomCacheAop(){
        return new BlossomCacheAop();
    }

    @Bean
    public BlossomCacheAnnotationParser blossomCacheAnnotationParser(){
        return new BlossomCacheAnnotationParser();
    }

    @Bean
    public BlossomCacheManagerFactory blossomCacheManagerFactory(){
        return new BlossomCacheManagerFactory();
    }

}
