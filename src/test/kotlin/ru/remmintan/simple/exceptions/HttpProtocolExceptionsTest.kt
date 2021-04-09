package ru.remmintan.simple.exceptions

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import ru.remmintan.simple.exceptions.dtos.ErrorDto
import ru.remmintan.simple.exceptions.testapp.TestApplication

@SpringBootTest(classes = [TestApplication::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class HttpProtocolExceptionsTest {

    @Autowired
    private lateinit var rest: TestRestTemplate

    private val mapper = jacksonObjectMapper()

    @Test
    fun methodNotAllowedTest() {
        val message = "Method POST not allowed. Allowed methods: GET."

        val result = rest.postForEntity("/test/badrequest", null, String::class.java)
        assertThat(result.statusCode).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED)
        assertThat(result.hasBody()).isTrue
        val error = mapper.readValue(result.body, ErrorDto::class.java)
        assertThat(error.message).isEqualTo(message)
    }

    @Test
    fun notFoundTest() {
        val result = rest.getForEntity("/path/that/doesnt/exist", String::class.java)
        assertThat(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun mediaTypeNotSupportedTest() {
        val invalidHeaders = HttpHeaders()
        invalidHeaders.contentType = MediaType.TEXT_PLAIN
        val invalidEntity = HttpEntity("", invalidHeaders);

        val errorMessage = "Content type 'text/plain;charset=UTF-8' not supported"

        val result = rest.postForEntity("/validation/mapping", invalidEntity, String::class.java)
        assertThat(result.statusCode).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
        assertThat(result.hasBody()).isTrue
        val error = mapper.readValue(result.body, ErrorDto::class.java)
        assertThat(error.message).isEqualTo(errorMessage)
    }

}