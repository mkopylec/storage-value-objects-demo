package com.github.mkopylec.storage.query

import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ContainerUpdater(
    private val properties: ContainerQueriesProperties,
    private val mongoDb: ReactiveMongoTemplate
) {
    suspend fun execute(request: (ReactiveMongoTemplate) -> Mono<*>) {
        request(mongoDb).handleAsynchronicity()
    }

    private suspend fun <T> Mono<T>.handleAsynchronicity(): Mono<T> = this.also {
        if (!properties.asynchronousModelUpdate) it.awaitSingleOrNull() else it.subscribe()
    }
}
