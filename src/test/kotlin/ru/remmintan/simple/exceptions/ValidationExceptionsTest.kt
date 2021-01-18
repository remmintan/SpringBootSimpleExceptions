package ru.remmintan.simple.exceptions

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.Resource
import org.springframework.http.*
import ru.remmintan.simple.exceptions.dtos.ErrorDto
import ru.remmintan.simple.exceptions.testapp.TestApplication

@SpringBootTest(classes = [TestApplication::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ValidationExceptionsTest {

    @Autowired
    private lateinit var rest: TestRestTemplate

    @Value("classpath:invalidDto.txt")
    private lateinit var invalidRequest: Resource

    @Value("classpath:validDto.txt")
    private lateinit var validRequest: Resource

    @Value("classpath:notReadableDto.txt")
    private lateinit var notReadableRequest: Resource

    private val mapper = jacksonObjectMapper()

    @Test
    fun validationErrorTest() {
        // assume
        val request = createRequest(invalidRequest)

        // act
        val result =  rest.postForEntity("/validation/mapping", request, String::class.java)

        // assert
        assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val error = mapper.readValue(result.body, ErrorDto::class.java)

        assertThat(error.message.split("\n"))
            .contains("Name must be present!", "Your age can't be less than 5.")
    }

    @Test
    fun validationOkTest() {
        // assume
        val request = createRequest(validRequest)

        // act
        val result =  rest.postForEntity("/validation/mapping", request, String::class.java)

        // assert
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.hasBody()).isFalse
    }

    @Test
    fun notReadableRequestTest() {
        // assume
        val request = createRequest(notReadableRequest)

        // act
        val result =  rest.postForEntity("/validation/mapping", request, String::class.java)

        // assert
        assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val error = mapper.readValue(result.body, ErrorDto::class.java)
        assertThat(error.message).isEqualTo("Can't read request message")
    }

    private fun createRequest(file: Resource): HttpEntity<String> {
        val body = file.inputStream.bufferedReader().readText()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        return HttpEntity(body, headers)
    }

}