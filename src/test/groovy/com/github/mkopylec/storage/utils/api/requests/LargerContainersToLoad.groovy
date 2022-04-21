package com.github.mkopylec.storage.utils.api.requests

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.http.RequestEntity

import static org.springframework.http.RequestEntity.get

class LargerContainersToLoad {

    @JsonIgnore
    BigDecimal minKilograms

    RequestEntity<Void> toEntity() {
        get("/containers?minKilograms=$minKilograms").build()
    }
}
