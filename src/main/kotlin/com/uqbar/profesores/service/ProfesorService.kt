package com.uqbar.profesores.service

import com.uqbar.profesores.domain.Profesor
import com.uqbar.profesores.repos.ProfesorRepository
import com.uqbar.profesores.serializer.ProfesorBasicoDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.server.ResponseStatusException

@Service
class ProfesorService {
    @Autowired
    lateinit var profesorRepository: ProfesorRepository

    @Transactional(readOnly = true)
    fun getProfesores() = this.profesorRepository.findAll()

    @Transactional(readOnly = true)
    fun getProfesor(@PathVariable id: Long) =
        this.profesorRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "El profesor con identificador $id no existe")
        }

    @Transactional
    fun actualizarProfesor(@RequestBody profesorNuevo: Profesor, @PathVariable id: Long): Profesor =
        profesorRepository.findById(id).map {
            it.nombreCompleto = profesorNuevo.nombreCompleto
            it.materias = profesorNuevo.materias
            profesorRepository.save(it)
            it
        }.orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "El profesor con identificador $id no existe")
        }

}