package com.github.mkopylec.storage.command.container.domain

import com.github.mkopylec.storage.command.common.domain.AggregateRepository
import com.github.mkopylec.storage.command.common.domain.events.EventPublisher

abstract class ContainerRepository(
    eventPublisher: EventPublisher
) : AggregateRepository<Container, ContainerIdentifier>(eventPublisher) {

    override fun missingAggregateViolation(identifier: ContainerIdentifier) = MissingContainer(identifier)

    override fun concurrentAggregateModificationViolation(identifier: ContainerIdentifier) = ConcurrentContainerModification(identifier)
}

class MissingContainer(identifier: ContainerIdentifier) : ContainerBusinessRuleViolation(listOf("identifier" to identifier))
class ConcurrentContainerModification(identifier: ContainerIdentifier) : ContainerBusinessRuleViolation(listOf("identifier" to identifier))
