package com.github.mkopylec.storage.utils.api

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

import java.lang.reflect.Type

@Component
class HttpResponseBodyReader {

    private ObjectMapper jsonMapper

    private HttpResponseBodyReader(ObjectMapper jsonMapper) {
        this.jsonMapper = jsonMapper
    }

    protected <B> B getBody(ResponseEntity<String> response, Class<B> bodyType) {
        try {
            jsonMapper.readValue(response.body, bodyType)
        } catch (Exception e) {
            null
        }
    }

    protected <B> B getBody(ResponseEntity<String> response, ParameterizedTypeReference<B> bodyType) {
        try {
            jsonMapper.readValue(response.body, new TypeReference<B>() {

                @Override
                Type getType() {
                    bodyType.type
                }
            })
        } catch (Exception e) {
            null
        }
    }
}
