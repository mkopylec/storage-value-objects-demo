package com.github.mkopylec.storage.command.container

import com.github.mkopylec.storage.command.container.domain.ContainerIdentifier
import com.github.mkopylec.storage.command.container.domain.WeightUnit
import com.github.mkopylec.storage.command.container.domain.WeightValue
import java.math.BigDecimal

class ContainerToAdd(
    identifier: String,
    maximumWeightValue: BigDecimal,
    maximumWeightUnit: String
) {
    val identifier by lazy { ContainerIdentifier(identifier) }
    val maximumWeightValue by lazy { WeightValue(maximumWeightValue) }
    val maximumWeightUnit by lazy { WeightUnit.from(maximumWeightUnit) }
}

class ContainerAddingViolation(
    private val violation: ContainerAddingViolationType
)

enum class ContainerAddingViolationType {

    INVALID_CONTAINER_IDENTIFIER,
    INVALID_WEIGHT_VALUE,
    INVALID_WEIGHT_UNIT
}
