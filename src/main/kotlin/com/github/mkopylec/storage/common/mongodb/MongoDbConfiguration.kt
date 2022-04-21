package com.github.mkopylec.storage.common.mongodb

import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit.MILLISECONDS

@Configuration
class MongoDbConfiguration(
    private val properties: MongoDbProperties
) {
    @Bean
    fun mongoDbSettings(): MongoClientSettingsBuilderCustomizer = MongoClientSettingsBuilderCustomizer { builder ->
        builder.applyToSocketSettings {
            it.connectTimeout(properties.timeout.connect.toMillis().toInt(), MILLISECONDS)
            it.readTimeout(properties.timeout.read.toMillis().toInt(), MILLISECONDS)
        }
    }
}
