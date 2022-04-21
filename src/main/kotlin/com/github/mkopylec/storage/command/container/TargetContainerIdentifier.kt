package com.github.mkopylec.storage.command.container

import com.github.mkopylec.storage.command.container.domain.ContainerIdentifier
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

class TargetContainerIdentifier(
    containerIdentifier: String
) {
    val containerIdentifier by lazy { ContainerIdentifier(containerIdentifier) }
}

@Component
class TargetContainerIdentifierConverter : Converter<String, TargetContainerIdentifier> {

    override fun convert(source: String) = TargetContainerIdentifier(source)
}
