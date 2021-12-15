package com.github.mkopylec.storage.core.container

import com.github.mkopylec.storage.core.common.InvariantViolation
import com.github.mkopylec.storage.core.container.Container.Identifier

class Container private constructor(
    val identifier: Identifier,
    val maximumWeight: Weight,
    items: List<Item>
) {

    constructor(identifier: Identifier, maximumWeight: Weight) : this(identifier, maximumWeight, mutableListOf())

    private val items: MutableList<Item> = items.toMutableList()

    val itemsQuantity: ItemsQuantity
        get() = ItemsQuantity(items.size)

    val itemsWeight: Weight
        get() = items.map { it.weight }.reduce { sum, addend -> sum + addend }

    fun insertItem(item: Item) {
        if (itemsWeight + item.weight > maximumWeight) throw ContainerMaximumWeightExceeded(item.identifier, item.weight, identifier, itemsWeight, maximumWeight)
        items.add(item)
    }

    fun <R> mapItems(transform: (Item) -> R): List<R> = items.map(transform)

    override fun toString(): String = "Container(identifier=$identifier, maximumWeight=$maximumWeight, items=$items)"

    @JvmInline
    value class Identifier(val value: String) {

        init {
            if (!value.matches(pattern)) throw InvalidContainerIdentifier(value)
        }

        override fun toString(): String = value

        companion object {

            private val pattern = Regex("[A-Z]{8}")
        }
    }

    @JvmInline
    value class ItemsQuantity(val value: Int) {

        override fun toString(): String = value.toString()
    }
}

private class InvalidContainerIdentifier(identifier: String) : InvariantViolation(listOf("identifier" to identifier))
private class ContainerMaximumWeightExceeded(
    itemIdentifier: Item.Identifier, itemWeight: Weight, containerIdentifier: Identifier, containerCurrentWeight: Weight, containerMaximumWeight: Weight,
) : InvariantViolation(
    listOf(
        "itemIdentifier" to itemIdentifier,
        "itemWeight" to itemWeight,
        "containerIdentifier" to containerIdentifier,
        "containerCurrentWeight" to containerCurrentWeight,
        "containerMaximumWeight" to containerMaximumWeight
    )
)
