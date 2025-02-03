package dn.mp_orders.domain.configuration.redis;

import dn.mp_orders.domain.configuration.CacheConfiguration;
import dn.mp_orders.domain.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(CacheConfiguration.class)
public class RedisConfig {

    @Value("${app.redis.host}")
    private String host;

    @Value("${app.redis.port}")
    private int port;
//
//    @Value("${cache.cacheNames.orderList}")
//    private String orders;
//
//    @Value("${cache.cacheNames.orderById}")
//    private String orderById;
//
//    @Value("${cache.cacheNames.orderAfterCreate}")
//    private String orderAfterCreate;
//
//    @Value("${cache.cacheNames.comment}")
//    private String comment;
//
//    @Value("${cache.cacheNames.orderListWithPagination}")
//    private String ordersWithPagination;





    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public <F,S> RedisTemplate<F, S> redisTemplate() {
        RedisTemplate<F, S> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }


//    @Bean
//    public RedisCacheManagerBuilderCustomizer cacheManagerBuilderCustomizer() {
//        return (builder) -> builder
//                .withCacheConfiguration(orderAfterCreate, RedisCacheConfiguration.defaultCacheConfig()
//                        .entryTtl(Duration.ofMinutes(5)))
//                .withCacheConfiguration(orderById,  RedisCacheConfiguration.defaultCacheConfig()
//                        .entryTtl(Duration.ofMinutes(10)))
//                .withCacheConfiguration(orders, RedisCacheConfiguration.defaultCacheConfig()
//                        .entryTtl(Duration.ofMinutes(10)))
//                .withCacheConfiguration(comment,
//                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(14)))
//                .withCacheConfiguration(ordersWithPagination, RedisCacheConfiguration.defaultCacheConfig()
//                        .entryTtl(Duration.ofMinutes(20))
//                                .serializeValuesWith(RedisSerializationContext.SerializationPair
//                                        .fromSerializer(new GenericJackson2JsonRedisSerializer())));
//    }

    @Bean
    public GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                                .fromSerializer(new Jackson2JsonRedisSerializer<>(OrderEntity.class)));
    }

//    @Bean
//    public RedisCacheManager redisCacheManager(){
//        RedisCacheConfiguration redisCacheConfiguration = redisCacheConfiguration();
//        return RedisCacheManager.builder(jedisConnectionFactory())
//                .cacheDefaults(redisCacheConfiguration)
//                .build();
//    }
    @Bean
    @ConditionalOnExpression("'${app.cache.cacheType}'.equals('redis')")
    public CacheManager cacheManager(CacheConfiguration cacheConfiguration,
                                     JedisConnectionFactory jedisConnectionFactory) {
        var defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        Map<String,RedisCacheConfiguration> cacheConfigurationMap = new HashMap<>();
        cacheConfiguration.getCacheNames().forEach(cache->cacheConfigurationMap.put(
                cache,
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(cacheConfiguration.getCaches().get(cache).getExpiry())
        ));
        return RedisCacheManager.builder(jedisConnectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(cacheConfigurationMap)
                .build();

    }

}
