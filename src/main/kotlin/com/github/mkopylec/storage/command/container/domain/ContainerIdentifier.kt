package com.github.mkopylec.storage.command.container.domain

import com.github.mkopylec.storage.command.common.domain.AggregateIdentifier

@JvmInline
value class ContainerIdentifier(val value: String) : AggregateIdentifier {

    init {
        if (!value.matches(pattern)) throw InvalidContainerIdentifier(value)
    }

    override fun toString(): String = value

    companion object {
        private val pattern = Regex("[A-Z]{8}")
    }
}

class InvalidContainerIdentifier(identifier: String) : ContainerBusinessRuleViolation(listOf("identifier" to identifier))
