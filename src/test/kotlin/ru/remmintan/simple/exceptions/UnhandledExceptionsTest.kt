package ru.remmintan.simple.exceptions

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sun.net.httpserver.HttpsServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import ru.remmintan.simple.exceptions.dtos.ErrorDto
import ru.remmintan.simple.exceptions.testapp.TestApplication

@SpringBootTest(classes = [TestApplication::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UnhandledExceptionsTest {

    @Autowired
    private lateinit var rest: TestRestTemplate

    private val mapper = jacksonObjectMapper()

    @Test
    fun unhandledExceptionTest() {
        val result = rest.getForEntity("/unhandled", String::class.java)
        assertThat(result.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        val error = mapper.readValue(result.body, ErrorDto::class.java)
        assertThat(error.message).isEqualTo("An error has occurred")
    }

}