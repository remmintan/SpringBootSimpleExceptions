package ru.remmintan.simple.exceptions.exceptions

import org.springframework.http.HttpStatus

class BadRequestException(
    message: String
) : ApiException(HttpStatus.BAD_REQUEST, message)