package ru.remmintan.simple.exceptions

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import ru.remmintan.simple.exceptions.dtos.ErrorDto
import ru.remmintan.simple.exceptions.testapp.TestApplication

@SpringBootTest(classes = [TestApplication::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ApiExceptionsTest {

    @Autowired
    private lateinit var rest: TestRestTemplate

    private val mapper = jacksonObjectMapper()


    @ParameterizedTest
    @CsvSource(
            "badrequest,BAD_REQUEST,Bad request test",
            "notfound,NOT_FOUND,Not found test",
            "unauthorized,UNAUTHORIZED,Unauthorized test",
            "forbidden,FORBIDDEN,Forbidden test",
            "internal,INTERNAL_SERVER_ERROR,Internal error test"
    )
    fun errorTest(path: String, status: HttpStatus, message: String) {
        val result = rest.getForEntity("/test/$path", String::class.java)
        assertThat(result.statusCode).isEqualTo(status)
        assertThat(result.hasBody()).isTrue
        val error = mapper.readValue(result.body, ErrorDto::class.java)
        assertThat(error.message).isEqualTo(message)
    }

    @Test
    fun okTest() {
        val result = rest.getForEntity("/test/ok", String::class.java)
        assertThat(result.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
        assertThat(result.hasBody()).isFalse
    }

}