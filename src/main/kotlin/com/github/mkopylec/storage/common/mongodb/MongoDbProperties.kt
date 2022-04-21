package com.github.mkopylec.storage.common.mongodb

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty
import java.time.Duration

@ConstructorBinding
@ConfigurationProperties("storage.common.infrastructure.mongodb")
class MongoDbProperties(
    @NestedConfigurationProperty
    val timeout: TimeoutProperties
)

@ConstructorBinding
class TimeoutProperties(
    val connect: Duration,
    val read: Duration
)
