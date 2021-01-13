package ru.remmintan.simple.exceptions.exceptions

import org.springframework.http.HttpStatus

class InternalServerException(message: String) : ApiException(HttpStatus.INTERNAL_SERVER_ERROR, message)