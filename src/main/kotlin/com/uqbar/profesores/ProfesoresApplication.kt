package com.uqbar.profesores

import graphql.language.StringValue
import graphql.schema.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.MalformedURLException
import java.net.URL

@SpringBootApplication
class ProfesoresApplication

fun main(args: Array<String>) {
	runApplication<ProfesoresApplication>(*args)
}
