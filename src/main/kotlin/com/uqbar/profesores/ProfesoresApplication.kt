package com.uqbar.profesores

import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration
import com.netflix.graphql.dgs.webmvc.autoconfigure.DgsWebMvcAutoConfiguration
import graphql.language.StringValue
import graphql.schema.*
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.MalformedURLException
import java.net.URL

@SpringBootApplication(exclude = arrayOf(DgsAutoConfiguration::class, DgsWebMvcAutoConfiguration::class))
class ProfesoresApplication

fun main(args: Array<String>) {
	runApplication<ProfesoresApplication>(*args)
}
