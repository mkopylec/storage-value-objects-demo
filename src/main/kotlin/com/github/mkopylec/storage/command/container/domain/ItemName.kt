package com.github.mkopylec.storage.command.container.domain

@JvmInline
value class ItemName(val value: String) {

    override fun toString(): String = value
}
