package com.github.mkopylec.storage.utils.api.requests

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.http.RequestEntity

import static org.springframework.http.RequestEntity.get

class ContainerToLoad {

    @JsonIgnore
    String identifier

    RequestEntity<Void> toEntity() {
        get("/containers/$identifier").build()
    }
}
