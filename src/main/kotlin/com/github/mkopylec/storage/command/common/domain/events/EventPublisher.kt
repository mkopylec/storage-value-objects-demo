package com.github.mkopylec.storage.command.common.domain.events

import org.springframework.stereotype.Component

@Component
class EventPublisher(
    private val handlers: List<EventHandler<*>>
) {
    suspend fun publishEvent(event: Event) = handlers.forEach { it.handleEvent(event) }
}
