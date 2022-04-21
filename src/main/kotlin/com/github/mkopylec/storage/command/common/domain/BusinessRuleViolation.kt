package com.github.mkopylec.storage.command.common.domain

abstract class BusinessRuleViolation(
    properties: List<Pair<String, Any?>> = emptyList(),
    cause: Throwable? = null
) : RuntimeException(properties.joinToString { "${it.first}=${it.second}" }, cause)
