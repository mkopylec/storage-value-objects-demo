package com.github.mkopylec.storage.utils.api.requests

import org.springframework.http.RequestEntity

import static org.springframework.http.RequestEntity.post

class ContainerToAdd {

    String identifier
    BigDecimal maximumWeightValue
    String maximumWeightUnit

    RequestEntity<ContainerToAdd> toEntity() {
        post("/containers").body(this)
    }
}
