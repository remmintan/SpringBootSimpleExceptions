package ru.remmintan.simple.exceptions

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.Resource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import ru.remmintan.simple.exceptions.dtos.ErrorDto
import ru.remmintan.simple.exceptions.testapp.TestApplication

@SpringBootTest(classes = [TestApplication::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource("classpath:custom-messages.properties")
class CustomMessagesTest {

    @Autowired
    private lateinit var rest: TestRestTemplate

    @Value("classpath:notReadableDto.txt")
    private lateinit var notReadableRequest: Resource

    private val mapper = jacksonObjectMapper()

    @Test
    fun unhandledExceptionTest() {
        val result = rest.getForEntity("/unhandled", String::class.java)
        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        val error = mapper.readValue(result.body, ErrorDto::class.java)
        Assertions.assertThat(error.message).isEqualTo("Custom message for unhandled exception")
    }

    @Test
    fun notReadableRequestTest() {
        // assume
        val body = notReadableRequest.inputStream.bufferedReader().readText()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = HttpEntity(body, headers)

        // act
        val result =  rest.postForEntity("/validation/mapping", request, String::class.java)

        // assert
        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val error = mapper.readValue(result.body, ErrorDto::class.java)
        Assertions.assertThat(error.message).isEqualTo("Custom message for not readable message")
    }

}