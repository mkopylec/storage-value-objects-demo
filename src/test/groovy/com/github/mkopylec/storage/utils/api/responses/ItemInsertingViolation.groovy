package com.github.mkopylec.storage.utils.api.responses

class ItemInsertingViolation {

    ItemInsertingViolationType violation
}

enum ItemInsertingViolationType {

    INVALID_CONTAINER_IDENTIFIER,
    INVALID_WEIGHT_VALUE,
    INVALID_WEIGHT_UNIT,
    CONTAINER_MAXIMUM_WEIGHT_EXCEEDED,
    MISSING_CONTAINER,
    CONCURRENT_CONTAINER_MODIFICATION
}
