package com.github.mkopylec.storage.query

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("storage.container.queries")
class ContainerQueriesProperties(
    val asynchronousModelUpdate: Boolean
)
