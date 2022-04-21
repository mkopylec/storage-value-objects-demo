package com.github.mkopylec.storage.utils.api

import org.springframework.http.HttpStatus

class HttpResponse<B, V> {

    private HttpStatus status
    private B body
    private V violation

    protected HttpResponse(HttpStatus status, B body, V violation) {
        this.status = status
        this.body = body
        this.violation = violation
    }

    HttpStatus getStatus() {
        status
    }

    B getBody() {
        body
    }

    V getViolation() {
        violation
    }
}
