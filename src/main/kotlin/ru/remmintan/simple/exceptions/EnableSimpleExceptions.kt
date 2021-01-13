package ru.remmintan.simple.exceptions

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportSelector
import org.springframework.core.type.AnnotationMetadata

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Import(EnableSimpleExceptions.SimpleExceptionsImporter::class)
annotation class EnableSimpleExceptions {
    class SimpleExceptionsImporter : ImportSelector {
        override fun selectImports(importingClassMetadata: AnnotationMetadata): Array<String> =
            arrayOf(SimpleExceptionsConfiguration::class.java.name)
    }

    class SimpleExceptionsConfiguration() {
        @Bean
        fun globalExceptionHandler(): RestExceptionHandler = RestExceptionHandler()
    }
}