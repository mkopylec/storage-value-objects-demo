package com.github.mkopylec.storage.core

import com.github.mkopylec.storage.core.common.InvariantViolation
import com.github.mkopylec.storage.core.container.Container.Identifier
import com.github.mkopylec.storage.core.container.Containers
import com.github.mkopylec.storage.core.container.Weight.Unit
import com.github.mkopylec.storage.core.container.Weight.Value
import java.math.BigDecimal

class ContainerAdder(
    private val containers: Containers
) {

    fun addContainer(containerToAdd: ContainerToAdd) = try {
        val container = containers.createContainer(containerToAdd)
        containers.saveContainer(container)
    } catch (e: InvariantViolation) {
        throw IllegalStateException("Container not added", e)
    }
}

data class ContainerToAdd(
    private val identifier: String,
    private val maximumWeightValue: BigDecimal,
    private val maximumWeightUnit: String
) {

    fun identifier() = Identifier(identifier)
    fun maximumWeightValue() = Value(maximumWeightValue)
    fun maximumWeightUnit() = Unit.from(maximumWeightUnit)
}
