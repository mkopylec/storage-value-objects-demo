package com.github.mkopylec.storage.command.container.domain.events

import com.github.mkopylec.storage.command.common.domain.events.Event
import com.github.mkopylec.storage.command.container.domain.ContainerIdentifier
import com.github.mkopylec.storage.command.container.domain.Weight

class ContainerAdded(
    val identifier: ContainerIdentifier,
    val maximumWeight: Weight
) : Event
