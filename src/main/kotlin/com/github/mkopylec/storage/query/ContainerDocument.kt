package com.github.mkopylec.storage.query

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.util.UUID

@Document("containers")
class ContainerDocument private constructor(
    @Id
    val identifier: String,
    val maximumWeight: WeightDocument,
    val items: ItemsDocument
) {
    class WeightDocument private constructor(
        val value: BigDecimal,
        val unit: String,
        val valueInKilograms: BigDecimal
    )

    class ItemsDocument private constructor(
        val items: List<ItemDocument>,
        val weight: WeightDocument
    )

    class ItemDocument private constructor(
        val identifier: UUID,
        val name: String,
        val weight: WeightDocument
    )
}
