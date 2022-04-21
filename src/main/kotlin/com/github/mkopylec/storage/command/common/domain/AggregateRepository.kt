package com.github.mkopylec.storage.command.common.domain

import com.github.mkopylec.storage.command.common.domain.AggregateSavingResult.CONCURRENT_MODIFICATION_REJECTED
import com.github.mkopylec.storage.command.common.domain.AggregateSavingResult.SUCCESS
import com.github.mkopylec.storage.command.common.domain.events.EventPublisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

abstract class AggregateRepository<A : Aggregate<I>, I : AggregateIdentifier>(
    protected open val eventPublisher: EventPublisher // Needs to be open, otherwise eventPublisher is null. Cannot use Spring ApplicationEventPublisher because there is no way to publish events synchronously, and this is needed for tests.
) {
    protected val logger: Logger = getLogger(javaClass)

    suspend fun save(aggregate: A) = when (saveAggregate(aggregate)) {
        SUCCESS -> aggregate.forEachEvent { eventPublisher.publishEvent(it) }
        CONCURRENT_MODIFICATION_REJECTED -> throw concurrentAggregateModificationViolation(aggregate.identifier)
    }

    suspend fun find(identifier: I): A = findAggregate(identifier) ?: throw missingAggregateViolation(identifier)

    protected abstract suspend fun saveAggregate(aggregate: A): AggregateSavingResult

    protected abstract suspend fun findAggregate(identifier: I): A?

    protected abstract fun missingAggregateViolation(identifier: I): BusinessRuleViolation

    protected abstract fun concurrentAggregateModificationViolation(identifier: I): BusinessRuleViolation
}

enum class AggregateSavingResult {

    SUCCESS, CONCURRENT_MODIFICATION_REJECTED
}
