package ru.remmintan.simple.exceptions.exceptions

import org.springframework.http.HttpStatus

class ForbiddenException(message: String) : ApiException(HttpStatus.FORBIDDEN, message)