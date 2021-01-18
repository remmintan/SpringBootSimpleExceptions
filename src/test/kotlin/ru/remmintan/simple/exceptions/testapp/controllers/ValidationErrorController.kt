package ru.remmintan.simple.exceptions.testapp.controllers

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.remmintan.simple.exceptions.testapp.TestDto
import javax.validation.Valid

@RestController
@RequestMapping("/validation")
class ValidationErrorController {

    @PostMapping("/mapping")
    fun testAction(@Valid @RequestBody dto: TestDto) {}

}