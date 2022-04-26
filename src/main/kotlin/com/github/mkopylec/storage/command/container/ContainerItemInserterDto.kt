package com.github.mkopylec.storage.command.container

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY
import com.github.mkopylec.storage.command.container.domain.Item
import com.github.mkopylec.storage.command.container.domain.ItemName
import com.github.mkopylec.storage.command.container.domain.WeightUnit
import com.github.mkopylec.storage.command.container.domain.WeightValue
import java.math.BigDecimal
import java.util.UUID

class ItemToInsert private constructor(
    private val name: String, // Need to be fields for OpenApi doc to work properly
    private val weightValue: BigDecimal,
    private val weightUnit: String
) {
    fun name() = ItemName(name)
    fun weightValue() = WeightValue(weightValue)
    fun weightUnit() = WeightUnit.from(weightUnit)
}

@JsonAutoDetect(fieldVisibility = ANY)
class InsertedItem private constructor(
    private val identifier: UUID
) {
    constructor(item: Item) : this(item.identifier.value)
}

@JsonAutoDetect(fieldVisibility = ANY)
class ItemInsertingViolation(
    private val violation: ItemInsertingViolationType
)

enum class ItemInsertingViolationType {

    INVALID_CONTAINER_IDENTIFIER,
    INVALID_WEIGHT_VALUE,
    INVALID_WEIGHT_UNIT,
    CONTAINER_MAXIMUM_WEIGHT_EXCEEDED,
    MISSING_CONTAINER,
    CONCURRENT_CONTAINER_MODIFICATION
}
