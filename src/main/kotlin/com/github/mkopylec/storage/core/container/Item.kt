package com.github.mkopylec.storage.core.container

import java.util.UUID
import java.util.UUID.randomUUID

class Item private constructor(
    val identifier: Identifier,
    val name: Name,
    val weight: Weight
) {

    constructor(name: Name, weight: Weight) : this(Identifier(randomUUID()), name, weight)

    override fun toString(): String = "Item(identifier=$identifier, name='$name', weight=$weight)"

    @JvmInline
    value class Identifier(val value: UUID) {

        override fun toString(): String = value.toString()
    }

    @JvmInline
    value class Name(val value: String) {

        override fun toString(): String = value
    }
}
