package ru.remmintan.simple.exceptions.exceptions

import org.springframework.http.HttpStatus

class NotFoundException(message: String) : ApiException(HttpStatus.NOT_FOUND, message)