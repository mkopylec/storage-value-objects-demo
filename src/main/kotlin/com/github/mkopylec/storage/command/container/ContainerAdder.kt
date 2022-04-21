package com.github.mkopylec.storage.command.container

import com.github.mkopylec.storage.command.container.ContainerAddingViolationType.INVALID_CONTAINER_IDENTIFIER
import com.github.mkopylec.storage.command.container.ContainerAddingViolationType.INVALID_WEIGHT_UNIT
import com.github.mkopylec.storage.command.container.ContainerAddingViolationType.INVALID_WEIGHT_VALUE
import com.github.mkopylec.storage.command.container.domain.Container
import com.github.mkopylec.storage.command.container.domain.ContainerBusinessRuleViolation
import com.github.mkopylec.storage.command.container.domain.ContainerRepository
import com.github.mkopylec.storage.command.container.domain.InvalidContainerIdentifier
import com.github.mkopylec.storage.command.container.domain.InvalidWeightUnit
import com.github.mkopylec.storage.command.container.domain.InvalidWeightValue
import com.github.mkopylec.storage.command.container.domain.Weight
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus

@ContainerRestController
class ContainerAdder(
    private val repository: ContainerRepository
) {
    private val logger: Logger = getLogger(javaClass)

    @PostMapping
    suspend fun addContainer(@RequestBody containerToAdd: ContainerToAdd) {
        val maximumWeight = Weight(containerToAdd.maximumWeightValue, containerToAdd.maximumWeightUnit)
        val container = Container(containerToAdd.identifier, maximumWeight)
        repository.save(container)
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidContainerIdentifier::class)
    fun handleInvalidContainerIdentifier(violation: InvalidContainerIdentifier): ContainerAddingViolation = handle(violation, INVALID_CONTAINER_IDENTIFIER)

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidWeightValue::class)
    fun handleInvalidWeightValue(violation: InvalidWeightValue): ContainerAddingViolation = handle(violation, INVALID_WEIGHT_VALUE)

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidWeightUnit::class)
    fun handleInvalidWeightUnit(violation: InvalidWeightUnit): ContainerAddingViolation = handle(violation, INVALID_WEIGHT_UNIT)

    private fun handle(violation: ContainerBusinessRuleViolation, violationType: ContainerAddingViolationType): ContainerAddingViolation {
        logger.warn("Cannot add container", violation)
        return ContainerAddingViolation(violationType)
    }
}
