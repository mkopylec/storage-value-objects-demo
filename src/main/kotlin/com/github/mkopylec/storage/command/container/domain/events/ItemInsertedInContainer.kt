package com.github.mkopylec.storage.command.container.domain.events

import com.github.mkopylec.storage.command.common.domain.events.Event
import com.github.mkopylec.storage.command.container.domain.ContainerIdentifier
import com.github.mkopylec.storage.command.container.domain.ItemIdentifier
import com.github.mkopylec.storage.command.container.domain.ItemName
import com.github.mkopylec.storage.command.container.domain.Weight

class ItemInsertedInContainer(
    val containerIdentifier: ContainerIdentifier,
    val identifier: ItemIdentifier,
    val name: ItemName,
    val weight: Weight
) : Event
