package com.github.mkopylec.storage.core.common

abstract class InvariantViolation(
    properties: List<Pair<String, Any?>> = emptyList(),
    cause: Throwable? = null
) : RuntimeException(properties.joinToString { "${it.first}=${it.second}" }, cause)
