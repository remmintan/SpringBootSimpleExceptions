# SpringBootSimpleExceptions [![](https://jitpack.io/v/SpringSimpleUtils/SpringBootSimpleExceptions.svg)](https://jitpack.io/#SpringSimpleUtils/SpringBootSimpleExceptions) [![EXAMPLE](https://github.com/SpringSimpleUtils/SpringBootSimpleExceptions/actions/workflows/gradle.yml/badge.svg)](https://github.com/SpringSimpleUtils/SpringBootSimpleExceptions/actions/workflows/gradle.yml)
Tiny lib which eliminates SpringBoot REST error handling boilerplate. Making errors "exception based".

**Step 1. Add sngle annotation to your application**
```
@EnableSimpleExceptions
```
**Step 2. Throw exception from any place of your application**
```
throw ForbiddenException("You are no allowed to use this service.")
```
**You are done!**
Now all your exceptions are handled and displayed to user properly, including correct status code and headers. Any error returns to client with correct statuc code, text and simple JSON object which contains message supplied to exception constructor.
```
403 Forbidden

{
    "message": "You are no allowed to use this service."
}
```
## Motivation
Error handling is strainforward feature, which any API needs and should be configured out of the box, but in Spring it's hard thing to do. We even have several guides on the internet [how to](https://www.baeldung.com/exception-handling-for-rest-with-spring) [do it](https://reflectoring.io/spring-boot-exception-handling/).
Main goal of this lib is to eliminate this hardness, keep SpringBoot's REST exception handling powerfull but simple. Giving users more time on writing business logic, not configuring boilerplate.
## Getting Started
Add lib annotation to your application.
```
import org.springframework.boot.autoconfigure.SpringBootApplication
import ru.remmintan.simple.exceptions.EnableSimpleExceptions

@SpringBootApplication
@EnableSimpleExceptions
class ExampleApplication {}
```
From this point all exceptions, throwed within application will be properly handled and returned to user as simple JSON objects with proper status code and status text. DTO [validation](#model-validation) and authentication are also handled by this library.
The most simple way top use this lib is just throw one of [special exception](#http-exceptions) from any part of your application.
```
import ru.remmintan.simple.exceptions.exceptions.*
...
throw BadRequestException("Username is too short")
```
Will convert to response with status code **400**:
```
{
    "message": "Username is too short."
}
```
## Installation
### Maven
**Step 1** Add the JitPack repository to your build file
```
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```
**Step 2** Add the dependency
```
<dependency>
    <groupId>com.github.SpringSimpleUtils</groupId>
    <artifactId>SpringBootSimpleExceptions</artifactId>
    <version>0.2.0</version>
</dependency>
```
### Gradle
**Step 1** Add the JitPack repository to your build file
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
**Step 2** Add the dependency
```
dependencies {
    implementation 'com.github.SpringSimpleUtils:SpringBootSimpleExceptions:0.2.0'
}
```
## Features
### Http Exceptions
Library has several preconfigured exceptions, which help throwing errors with standart error status codes. Message, supplied to exception's constructor converted to simple JSON containing only one field "message". This JSON then returns to client.
All exceptions can be found in package `ru.remmintan.simple.exceptions.exceptions`.
**400 Bad Request** - `throw BadRequestException("Example message")`
**401 Unauthorized** - `throw UnauthorizedException("Example message")`
**403 Forbidden** - `throw ForbiddenException("Example message")`
**404 Bad Request** - `throw NotFoundException("Example message")`
**500 Internal Server Error** - `throw InternalServerException("Example message")`
### Model Validation
DTO and constraints validation are also covered by this library. When you add validation annotations to any model and validation fails, all the validation errors return to client within `message` field. See example below.
```
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
```
If one of the constraints fails to pass client will get JSON object with message error text.
```
{
    "message": "Name must be present!\nYour age can't be less than 5."
}
```
This message can be shown to the user on frontend without any cahnges. Simple enough!
### Http Protocol Errors
Library also handles some HTTP protocol errors and send proper message to client:
1. [Method not allowed](https://developer.mozilla.org/ru/docs/Web/HTTP/Status/405) - sends back proper `Allow` header and JSON object with message, containing allowed headers.
2. [Unsupported media type](https://developer.mozilla.org/ru/docs/Web/HTTP/Status/415) - sends back message from `HttpMediaTypeNotSupportedException`.
3. [MessageNotReadable](https://docs.spring.io/spring-framework/docs/4.0.x/javadoc-api/org/springframework/http/converter/HttpMessageNotReadableException.html) - sends 400 BadRequest with standart message.
### Globlal Handler
Global handler handles all exceptions, not handled by any previous case. To prevent leak of any sensetive information this handler logs an exception at the ERROR logging level and returns to client:
```
500 InternalServerError

{
    "message": "An error has occured"
}
```
`An error has occured` is a standart message which can be [overriden](#overriding-standart-messages)
### Overriding standart messages
**MessageNotReadable** and **Global** error handlers return standart messages: `Can't read request message` and `An error has occurred`.
This behaviour can be overriden by adding two properties to application's props file:
```
simple.exceptions.messages.globalhandler: Custom message for unhandled exception
simple.exceptions.messages.notreadable: Custom message for not readable message
```
## Contribution
This lib is still on the very early stage of development. I will appreciate any help. Feel free to open issues or PRs.
[If you ❤️ press ⭐](https://github.com/SpringSimpleUtils/SpringBootSimpleExceptions/stargazers)
