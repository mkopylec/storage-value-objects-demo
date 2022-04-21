package com.github.mkopylec.storage.utils.api.responses

class ContainerAddingViolation {

    ContainerAddingViolationType violation
}

enum ContainerAddingViolationType {

    INVALID_CONTAINER_IDENTIFIER,
    INVALID_WEIGHT_VALUE,
    INVALID_WEIGHT_UNIT
}
