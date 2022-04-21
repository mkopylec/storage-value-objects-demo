package com.github.mkopylec.storage.command.container

import com.github.mkopylec.storage.command.container.ItemInsertingViolationType.CONCURRENT_CONTAINER_MODIFICATION
import com.github.mkopylec.storage.command.container.ItemInsertingViolationType.CONTAINER_MAXIMUM_WEIGHT_EXCEEDED
import com.github.mkopylec.storage.command.container.ItemInsertingViolationType.INVALID_CONTAINER_IDENTIFIER
import com.github.mkopylec.storage.command.container.ItemInsertingViolationType.INVALID_WEIGHT_UNIT
import com.github.mkopylec.storage.command.container.ItemInsertingViolationType.INVALID_WEIGHT_VALUE
import com.github.mkopylec.storage.command.container.ItemInsertingViolationType.MISSING_CONTAINER
import com.github.mkopylec.storage.command.container.domain.ConcurrentContainerModification
import com.github.mkopylec.storage.command.container.domain.ContainerBusinessRuleViolation
import com.github.mkopylec.storage.command.container.domain.ContainerMaximumWeightExceeded
import com.github.mkopylec.storage.command.container.domain.ContainerRepository
import com.github.mkopylec.storage.command.container.domain.InvalidContainerIdentifier
import com.github.mkopylec.storage.command.container.domain.InvalidWeightUnit
import com.github.mkopylec.storage.command.container.domain.InvalidWeightValue
import com.github.mkopylec.storage.command.container.domain.Item
import com.github.mkopylec.storage.command.container.domain.MissingContainer
import com.github.mkopylec.storage.command.container.domain.Weight
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus

@ContainerRestController
class ContainerItemInserter(
    private val repository: ContainerRepository
) {
    private val logger: Logger = getLogger(javaClass)

    @PutMapping("/{identifier}/items")
    suspend fun insertItemInContainer(@PathVariable identifier: TargetContainerIdentifier, @RequestBody itemToInsert: ItemToInsert): InsertedItem {
        val weight = Weight(itemToInsert.weightValue, itemToInsert.weightUnit)
        val item = Item(itemToInsert.name, weight)
        val container = repository.find(identifier.containerIdentifier)
        container.insertItem(item)
        repository.save(container)
        return InsertedItem(item)
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidContainerIdentifier::class)
    fun handleInvalidContainerIdentifier(violation: InvalidContainerIdentifier): ItemInsertingViolation = handle(violation, INVALID_CONTAINER_IDENTIFIER)

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidWeightValue::class)
    fun handleInvalidWeightValue(violation: InvalidWeightValue): ItemInsertingViolation = handle(violation, INVALID_WEIGHT_VALUE)

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidWeightUnit::class)
    fun handleInvalidWeightUnit(violation: InvalidWeightUnit): ItemInsertingViolation = handle(violation, INVALID_WEIGHT_UNIT)

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(ContainerMaximumWeightExceeded::class)
    fun handleContainerMaximumWeightExceeded(violation: ContainerMaximumWeightExceeded): ItemInsertingViolation = handle(violation, CONTAINER_MAXIMUM_WEIGHT_EXCEEDED)

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(MissingContainer::class)
    fun handleMissingContainer(violation: MissingContainer): ItemInsertingViolation = handle(violation, MISSING_CONTAINER)

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(ConcurrentContainerModification::class)
    fun handleConcurrentContainerModification(violation: ConcurrentContainerModification): ItemInsertingViolation = handle(violation, CONCURRENT_CONTAINER_MODIFICATION)

    private fun handle(violation: ContainerBusinessRuleViolation, violationType: ItemInsertingViolationType): ItemInsertingViolation {
        logger.warn("Cannot insert item in container", violation)
        return ItemInsertingViolation(violationType)
    }
}
