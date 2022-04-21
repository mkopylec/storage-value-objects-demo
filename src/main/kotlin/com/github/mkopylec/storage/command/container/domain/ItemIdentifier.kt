package com.github.mkopylec.storage.command.container.domain

import java.util.UUID

@JvmInline
value class ItemIdentifier(val value: UUID) {

    override fun toString(): String = value.toString()
}
