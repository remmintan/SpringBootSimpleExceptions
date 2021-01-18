package ru.remmintan.simple.exceptions.testapp

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class TestDto(
    @field:NotBlank(message = "Name must be present!")
    val name: String,
    @field:Min(value = 5, message = "Your age can't be less than 5.")
    val age: Int,
    @field:NotNull(message = "Description can't be null")
    val description: String
)
