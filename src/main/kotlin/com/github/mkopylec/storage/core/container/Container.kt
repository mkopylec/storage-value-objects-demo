package com.github.mkopylec.storage.core.container

class Container private constructor(
    val identifier: String,
    val maximumWeight: Weight,
    items: List<Item>
) {

    constructor(identifier: String, maximumWeight: Weight) : this(identifier, maximumWeight, mutableListOf())

    init {
        if (!identifier.matches(identifierPattern)) throw IllegalArgumentException("Invalid container identifier: identifier=$identifier")
    }

    private val items: MutableList<Item> = items.toMutableList()

    val itemsQuantity: Int
        get() = items.size

    val itemsWeight: Weight?
        get() = items.map { it.weight }.reduceOrNull { sum, addend -> sum + addend }

    fun insertItem(item: Item) {
        val weight = itemsWeight
        if (weight != null && weight + item.weight > maximumWeight)
            throw IllegalArgumentException(
                "Container maximum weight exceeded: " +
                        "itemIdentifier=${item.identifier}, " +
                        "itemWeight=${item.weight}, " +
                        "identifier=$identifier, " +
                        "itemsWeight=$weight, " +
                        "maximumWeight=$maximumWeight"
            )
        items.add(item)
    }

    fun <R> mapItems(transform: (Item) -> R): List<R> = items.map(transform)

    override fun toString(): String = "Container(identifier='$identifier', maximumWeight=$maximumWeight, items=$items)"

    companion object {

        private val identifierPattern = Regex("[A-Z]{8}")
    }
}
