package com.peacn.blossom.cache.cache;

import com.peach.blossom.cache.aop.PointGoldCacheAop;
import com.peach.blossom.cache.parser.PointGoldCacheAnnotationParser;
import org.springframework.context.annotation.Bean;

/**
 * @author taoruanliang
 * @date 2022/2/16 14:31
 */
public class PointGoldCacheConfiguration {

    @Bean
    public PointGoldCacheAop pointGoldCacheAop(){
        return new PointGoldCacheAop();
    }

    @Bean
    public PointGoldCacheAnnotationParser pointGoldCacheAnnotationParser(){
        return new PointGoldCacheAnnotationParser();
    }

    @Bean
    public PointGoldCacheManagerFactory pointGoldCacheManagerFactory(){
        return new PointGoldCacheManagerFactory();
    }

}
