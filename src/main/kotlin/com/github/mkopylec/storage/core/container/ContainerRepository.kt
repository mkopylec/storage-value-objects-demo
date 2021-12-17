package com.github.mkopylec.storage.core.container

import com.github.mkopylec.storage.core.container.Container.Identifier

interface ContainerRepository {

    fun save(container: Container)
    fun findByIdentifier(identifier: Identifier): Container?
}
