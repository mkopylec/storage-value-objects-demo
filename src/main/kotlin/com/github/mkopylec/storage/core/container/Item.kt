package com.github.mkopylec.storage.core.container

import java.util.UUID
import java.util.UUID.randomUUID

class Item private constructor(
    val identifier: UUID,
    val name: String,
    val weight: Weight
) {

    constructor(name: String, weight: Weight) : this(randomUUID(), name, weight)

    override fun toString(): String = "Item(identifier=$identifier, name='$name', weight=$weight)"
}
