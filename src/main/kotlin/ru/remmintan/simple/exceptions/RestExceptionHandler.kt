package ru.remmintan.simple.exceptions

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import ru.remmintan.simple.exceptions.dtos.ErrorDto
import ru.remmintan.simple.exceptions.exceptions.ApiException

private val logger = KotlinLogging.logger {}

@ControllerAdvice
class RestExceptionHandler {

    @Value("${'$'}{simple.exceptions.messages.default:No message}")
    private lateinit var defaultMessage: String

    @Value("${'$'}{simple.exceptions.messages.global-handler}")
    private lateinit var globalHandlerMessage: String

    @ExceptionHandler(ApiException::class)
    fun handleApiException(ex: ApiException) : ResponseEntity<ErrorDto> =
        ResponseEntity.status(ex.code).body(ErrorDto(ex.message ?: defaultMessage))

    @ExceptionHandler(Exception::class)
    fun handleAll(ex: Exception): ErrorDto {
        logger.error(ex) {"Unhandled exception in application!"}
        return ErrorDto(globalHandlerMessage)
    }

    @ExceptionHandler(BindException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBindException(ex: BindException): ErrorDto = parseBindingResult(ex.bindingResult)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException): ErrorDto
        = parseBindingResult(ex.bindingResult)

    private fun parseBindingResult(br: BindingResult): ErrorDto {
        val errors = br.allErrors.map {it.defaultMessage}.joinToString("\n")
        return ErrorDto(errors)
    }
}