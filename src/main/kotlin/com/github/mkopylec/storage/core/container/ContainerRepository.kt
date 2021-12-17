package com.github.mkopylec.storage.core.container

interface ContainerRepository {

    fun save(container: Container)
    fun findByIdentifier(identifier: String): Container?
}
