package com.inventoryapp.inventory_system.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 1. Create a new Jackson JSON Serializer for serializing cache values as JSON
        // We configure it to handle Java 8 Time and add type information
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper());

        // 2. Create a cache configuration
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                //Set a default expiration time (eg. 30 minutes) - good practice for caches
                .entryTtl(Duration.ofMinutes(30))
                // Key Serializer: String (human-readable)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // Value Serializer: JSON ( our new JACKSON serializer)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
                .disableCachingNullValues(); //Optional: avoid caching nulls

        // 3. Build the cache manager with this new configuration
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config)
                .build();
    }

    /**
     * Creates a pre-configured ObjectMapper for Jackson.
     * This is what enables the "@class" property to be stored,
     * which is VITAL for Spring to know what class to deserialize the JSON back into.
     */
    private ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Add support for Java 8 time types (like LocalDateTime)

        // This is the key: It stores the class name (e.g., "@class": "com.inventoryapp.inventorysystem.model.Product")
        // in the JSON. This allows deserialization of complex/generic types.
        mapper.activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder()
                        .allowIfBaseType(Object.class)
                        .build(),
                ObjectMapper.DefaultTyping.NON_FINAL
        );
        return  mapper;
    }
}
