package com.github.mkopylec.storage.command.container.domain

import com.github.mkopylec.storage.command.common.domain.BusinessRuleViolation

sealed class ContainerBusinessRuleViolation(
    properties: List<Pair<String, Any?>> = emptyList(),
    cause: Throwable? = null
) : BusinessRuleViolation(properties, cause)
