package com.github.mkopylec.storage.command.container

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY
import com.github.mkopylec.storage.command.container.domain.ContainerIdentifier
import com.github.mkopylec.storage.command.container.domain.WeightUnit
import com.github.mkopylec.storage.command.container.domain.WeightValue
import java.math.BigDecimal

class ContainerToAdd private constructor(
    private val identifier: String, // Need to be fields for OpenApi doc to work properly
    private val maximumWeightValue: BigDecimal,
    private val maximumWeightUnit: String
) {
    fun identifier() = ContainerIdentifier(identifier)
    fun maximumWeightValue() = WeightValue(maximumWeightValue)
    fun maximumWeightUnit() = WeightUnit.from(maximumWeightUnit)
}

@JsonAutoDetect(fieldVisibility = ANY)
class ContainerAddingViolation(
    private val violation: ContainerAddingViolationType
)

enum class ContainerAddingViolationType {

    INVALID_CONTAINER_IDENTIFIER,
    INVALID_WEIGHT_VALUE,
    INVALID_WEIGHT_UNIT
}
