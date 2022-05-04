package com.uqbar.profesores.controller

import com.uqbar.profesores.domain.Profesor
import com.uqbar.profesores.serializer.ProfesorBasicoDTO
import com.uqbar.profesores.service.ProfesorService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["*"])
class ProfesorController {
    @Autowired
    lateinit var profesorService: ProfesorService

    @GetMapping("/profesores")
    @Operation(summary = "Devuelve todos los profesores, sin las materias")
    fun getProfesores() = profesorService.getProfesores().map { ProfesorBasicoDTO(it) }.toList()

    @GetMapping("/profesores/{id}")
    @Operation(summary = "Devuelve un profesor, con sus materias")
    fun getProfesor(@PathVariable id: Long) = profesorService.getProfesor(id)

    @PutMapping("/profesores/{id}")
    @Operation(summary = "Actualiza un profesor")
    fun actualizarProfesor(@RequestBody profesorNuevo: Profesor, @PathVariable id: Long) =
        profesorService.actualizarProfesor(profesorNuevo, id)
}