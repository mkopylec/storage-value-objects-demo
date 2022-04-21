package com.github.mkopylec.storage.command.container.domain

import com.github.mkopylec.storage.command.common.domain.Aggregate
import com.github.mkopylec.storage.command.common.domain.AggregateVersion
import com.github.mkopylec.storage.command.container.domain.events.ContainerAdded
import com.github.mkopylec.storage.command.container.domain.events.ItemInsertedInContainer

class Container private constructor(
    identifier: ContainerIdentifier,
    version: AggregateVersion,
    val maximumWeight: Weight,
    val items: Items
) : Aggregate<ContainerIdentifier>(identifier, version) {

    constructor(identifier: ContainerIdentifier, maximumWeight: Weight) : this(identifier, AggregateVersion(), maximumWeight, Items()) {
        val event = ContainerAdded(identifier, maximumWeight)
        raiseEvent(event)
    }

    fun insertItem(item: Item) {
        if (items.weight + item.weight > maximumWeight) {
            throw ContainerMaximumWeightExceeded(item.identifier, item.weight, identifier, items.weight, maximumWeight)
        }
        items.insert(item)
        val event = ItemInsertedInContainer(identifier, item.identifier, item.name, item.weight)
        raiseEvent(event)
    }

    override fun toString(): String = "Container(identifier='$identifier', maximumWeight=$maximumWeight, items=$items)"

    companion object {
        fun fromPersistentState(identifier: ContainerIdentifier, version: AggregateVersion, maximumWeight: Weight, items: Items) =
            Container(identifier, version, maximumWeight, items)
    }
}

class ContainerMaximumWeightExceeded(
    itemIdentifier: ItemIdentifier, itemWeight: Weight, containerIdentifier: ContainerIdentifier, containerCurrentWeight: Weight, containerMaximumWeight: Weight,
) : ContainerBusinessRuleViolation(
    listOf(
        "itemIdentifier" to itemIdentifier,
        "itemWeight" to itemWeight,
        "containerIdentifier" to containerIdentifier,
        "containerCurrentWeight" to containerCurrentWeight,
        "containerMaximumWeight" to containerMaximumWeight
    )
)
