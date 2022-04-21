package com.github.mkopylec.storage.command.common.domain.events

import kotlin.reflect.KClass

abstract class EventHandler<E : Event>(
    private val supportedEvent: KClass<E>
) {
    @Suppress("UNCHECKED_CAST")
    suspend fun handleEvent(event: Event) {
        if (supportedEvent == event::class) {
            handle(event as E)
        }
    }

    protected abstract suspend fun handle(event: E)
}
