package com.github.mkopylec.storage.query

import com.github.mkopylec.storage.query.ContainerViewDocument.Companion.CONTAINER_MAXIMUM_WEIGHT_IN_KILOGRAMS_FIELD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import java.math.BigDecimal

@ContainerRestController
class ContainerLoader(
    private val mongoDb: ReactiveMongoTemplate
) {
    @GetMapping("/{identifier}") // Immediately consistent query result
    suspend fun loadContainer(@PathVariable identifier: String): ContainerView? =
        mongoDb.findById(identifier, ContainerDocument::class.java).awaitSingleOrNull()
            ?.let { ContainerView(it) }

    @GetMapping // Eventually consistent query result
    suspend fun loadLargerContainers(@RequestParam minKilograms: BigDecimal): Flow<ContainerView> =
        mongoDb.find(
            query(where(CONTAINER_MAXIMUM_WEIGHT_IN_KILOGRAMS_FIELD).gte(minKilograms)),
            ContainerViewDocument::class.java
        ).asFlow().map { ContainerView(it) }
}
