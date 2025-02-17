package com.example.musinsa.config

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CaffeineCacheConfig {
    @Bean
    fun cache(): Cache<String, Any> {
        return Caffeine.newBuilder()
            .maximumSize(100)
            .build()
    }
}