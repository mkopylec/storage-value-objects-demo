package com.github.mkopylec.storage.command.container.infrastructure

import com.github.mkopylec.storage.command.common.domain.AggregateSavingResult
import com.github.mkopylec.storage.command.common.domain.AggregateSavingResult.CONCURRENT_MODIFICATION_REJECTED
import com.github.mkopylec.storage.command.common.domain.AggregateSavingResult.SUCCESS
import com.github.mkopylec.storage.command.common.domain.events.EventPublisher
import com.github.mkopylec.storage.command.container.domain.Container
import com.github.mkopylec.storage.command.container.domain.ContainerIdentifier
import com.github.mkopylec.storage.command.container.domain.ContainerRepository
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Repository

@Repository
class MongoDbContainerRepository(
    eventPublisher: EventPublisher,
    private val mongoDb: ReactiveMongoTemplate
) : ContainerRepository(eventPublisher) {

    override suspend fun saveAggregate(aggregate: Container): AggregateSavingResult = try {
        mongoDb.save(ContainerDocument(aggregate)).awaitSingleOrNull()
        SUCCESS
    } catch (e: OptimisticLockingFailureException) {
        logger.error(e.message, e)
        CONCURRENT_MODIFICATION_REJECTED
    }

    override suspend fun findAggregate(identifier: ContainerIdentifier): Container? =
        mongoDb.findById(identifier.value, ContainerDocument::class.java).awaitSingleOrNull()?.toContainer()
}
