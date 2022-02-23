package com.github.mkopylec.storage.core

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
    } catch (e: IllegalArgumentException) {
        throw IllegalStateException("Container not added", e)
    }
}

class ContainerToAdd(
    identifier: String,
    maximumWeightValue: BigDecimal,
    maximumWeightUnit: String
) {

    val identifier = Identifier(identifier)
    val maximumWeightValue = Value(maximumWeightValue)
    val maximumWeightUnit = Unit.from(maximumWeightUnit)

    override fun toString(): String = "ContainerToAdd(identifier=$identifier, maximumWeightValue=$maximumWeightValue, maximumWeightUnit=$maximumWeightUnit)"
}
