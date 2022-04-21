package com.github.mkopylec.storage.command.common.domain

import com.github.mkopylec.storage.command.common.domain.events.Event

abstract class Aggregate<I : AggregateIdentifier>(
    val identifier: I,
    val version: AggregateVersion
) {
    private val events: MutableList<Event> = mutableListOf()

    suspend fun forEachEvent(action: suspend (Event) -> Unit) = events.forEach { action(it) }

    protected fun raiseEvent(event: Event) {
        events.add(event)
    }
}
