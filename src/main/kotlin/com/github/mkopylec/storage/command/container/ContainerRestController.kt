package com.github.mkopylec.storage.command.container

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.annotation.AnnotationTarget.CLASS

@Target(CLASS)
@RestController
@RequestMapping("/containers")
annotation class ContainerRestController
