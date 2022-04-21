package com.github.mkopylec.storage.utils.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.mkopylec.storage.utils.api.requests.ContainerToAdd
import com.github.mkopylec.storage.utils.api.requests.ContainerToLoad
import com.github.mkopylec.storage.utils.api.requests.ItemToInsert
import com.github.mkopylec.storage.utils.api.requests.LargerContainersToLoad
import com.github.mkopylec.storage.utils.api.responses.ContainerAddingViolation
import com.github.mkopylec.storage.utils.api.responses.ContainerView
import com.github.mkopylec.storage.utils.api.responses.InsertedItem
import com.github.mkopylec.storage.utils.api.responses.ItemInsertingViolation
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Component

@Component
class StorageHttpClient {

    private TestRestTemplate httpClient
    private HttpResponseBodyReader bodyReader
    private ObjectMapper mapper

    private StorageHttpClient(TestRestTemplate httpClient, HttpResponseBodyReader bodyReader, ObjectMapper mapper) {
        this.httpClient = httpClient
        this.bodyReader = bodyReader
        this.mapper = mapper
    }

    HttpResponse<Void, ContainerAddingViolation> addContainer(ContainerToAdd request) {
        sendRequest(request.toEntity(), Void, ContainerAddingViolation)
    }

    HttpResponse<InsertedItem, ItemInsertingViolation> insertItemInContainer(ItemToInsert request) {
        sendRequest(request.toEntity(), InsertedItem, ItemInsertingViolation)
    }

    HttpResponse<ContainerView, Void> loadContainer(ContainerToLoad request) {
        sendRequest(request.toEntity(), ContainerView, Void)
    }

    HttpResponse<List<ContainerView>, Void> loadLargerContainers(LargerContainersToLoad request) {
        sendRequest(request.toEntity(), new ParameterizedTypeReference<List<ContainerView>>() {}, Void)
    }

    private <B, V> HttpResponse<B, V> sendRequest(RequestEntity request, Class<B> responseBodyType, Class<V> responseViolationType) {
        def response = httpClient.exchange(request, String)
        def body = bodyReader.getBody(response, responseBodyType)
        def violation = bodyReader.getBody(response, responseViolationType)
        new HttpResponse<>(response.statusCode, body, violation)
    }

    private <B, V> HttpResponse<B, V> sendRequest(RequestEntity request, ParameterizedTypeReference<B> responseBodyType, Class<V> responseViolationType) {
        def response = httpClient.exchange(request, String)
        def body = bodyReader.getBody(response, responseBodyType)
        def violation = bodyReader.getBody(response, responseViolationType)
        new HttpResponse<>(response.statusCode, body, violation)
    }
}
