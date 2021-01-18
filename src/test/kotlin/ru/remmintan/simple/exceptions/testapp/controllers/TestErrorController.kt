package ru.remmintan.simple.exceptions.testapp.controllers

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.remmintan.simple.exceptions.exceptions.*

@RestController
@RequestMapping("/test")
class TestErrorController {

    @GetMapping("/badrequest")
    fun badRequest() {
        throw BadRequestException("Bad request test")
    }

    @GetMapping("/notfound")
    fun notFound() {
        throw NotFoundException("Not found test")
    }

    @GetMapping("/unauthorized")
    fun unauthorized() {
        throw UnauthorizedException("Unauthorized test")
    }

    @GetMapping("/forbidden")
    fun forbidden() {
        throw ForbiddenException("Forbidden test")
    }

    @GetMapping("/internal")
    fun internal() {
        throw InternalServerException("Internal error test")
    }

    @GetMapping("/ok")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun ok() {}

}