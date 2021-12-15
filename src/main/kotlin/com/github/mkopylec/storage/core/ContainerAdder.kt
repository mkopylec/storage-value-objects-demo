package com.github.mkopylec.storage.core

import com.github.mkopylec.storage.core.container.Containers
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

data class ContainerToAdd(
    val identifier: String,
    val maximumWeightValue: BigDecimal,
    val maximumWeightUnit: String
)
