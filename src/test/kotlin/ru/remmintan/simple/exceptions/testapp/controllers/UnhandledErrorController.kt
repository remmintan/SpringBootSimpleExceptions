package ru.remmintan.simple.exceptions.testapp.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.RuntimeException

@RestController
@RequestMapping("/unhandled")
class UnhandledErrorController {

    @GetMapping
    fun errorAction() {
        throw RuntimeException("Some unhandled exception")
    }

}