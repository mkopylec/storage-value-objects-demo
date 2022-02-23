package com.github.mkopylec.storage.core

import com.github.mkopylec.storage.core.common.InvariantViolation

abstract class UseCaseViolation(violation: InvariantViolation) : RuntimeException(violation) {

    val code: String = camelCasePattern.replace(requireNotNull(violation::class.simpleName)) { "_${it.value}" }.uppercase()

    override val message: String
        get() = "$code | $cause"

    companion object {

        private val camelCasePattern = Regex("(?<=[a-zA-Z])[A-Z]")
    }
}
