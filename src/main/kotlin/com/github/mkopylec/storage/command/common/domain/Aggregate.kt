package com.github.mkopylec.storage.command.common.domain

import com.github.mkopylec.storage.command.common.domain.events.Event
import com.github.mkopylec.storage.command.common.domain.events.EventPublisher

abstract class Aggregate<I : AggregateIdentifier>(
    val identifier: I,
    val version: AggregateVersion
) {
    private val events: MutableList<Event> = mutableListOf()

    suspend fun publishEvents(publisher: EventPublisher) = events.forEach { publisher.publishEvent(it) }

    protected fun raiseEvent(event: Event) {
        events.add(event)
    }
}
