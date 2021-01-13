package ru.remmintan.simple.exceptions.exceptions

import org.springframework.http.HttpStatus

abstract class ApiException(
    val code: HttpStatus,
    message: String
) : RuntimeException(message = message)