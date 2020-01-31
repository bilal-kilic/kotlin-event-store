package com.example.domain

import com.example.infra.BusinessException

fun validate(expression: Boolean, lazyMessage: () -> Any) {
    if (!expression) {
        val message = lazyMessage()
        throw BusinessException(message.toString())
    }
}