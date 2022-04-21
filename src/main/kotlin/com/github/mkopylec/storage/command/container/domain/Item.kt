package com.github.mkopylec.storage.command.container.domain

import java.util.UUID.randomUUID

class Item private constructor(
    val identifier: ItemIdentifier,
    val name: ItemName,
    val weight: Weight
) {
    constructor(name: ItemName, weight: Weight) : this(ItemIdentifier(randomUUID()), name, weight)

    override fun toString(): String = "Item(identifier=$identifier, name='$name', weight=$weight)"

    companion object {
        fun fromPersistentState(identifier: ItemIdentifier, name: ItemName, weight: Weight) = Item(identifier, name, weight)
    }
}
