package com.github.mkopylec.storage.utils.api.requests

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.http.RequestEntity

import static org.springframework.http.RequestEntity.put

class ItemToInsert {

    @JsonIgnore
    String containerIdentifier
    String name
    BigDecimal weightValue
    String weightUnit

    RequestEntity<ItemToInsert> toEntity() {
        put("/containers/$containerIdentifier/items").body(this)
    }
}
