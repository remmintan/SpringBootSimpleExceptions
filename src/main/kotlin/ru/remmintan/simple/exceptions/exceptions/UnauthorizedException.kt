package ru.remmintan.simple.exceptions.exceptions

import org.springframework.http.HttpStatus

class UnauthorizedException(message: String) : ApiException(HttpStatus.UNAUTHORIZED, message)