package com.uqbar.profesores.service

import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException
import com.uqbar.profesores.repos.ProfesorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable

@Service
class ProfesorService {
    @Autowired
    lateinit var profesorRepository: ProfesorRepository

    @Transactional(readOnly = true)
    fun getProfesoresByNombre(nombreFilter: String) = this.profesorRepository.findAllByNombreOrApellido(nombreFilter)

    @Transactional(readOnly = true)
    fun getProfesor(@PathVariable id: Long) =
        this.profesorRepository.findById(id).orElseThrow {
            DgsEntityNotFoundException("El profesor con identificador $id no existe")
        }

//    @Transactional
//    fun actualizarProfesor(@RequestBody profesorNuevo: Profesor, @PathVariable id: Long): Profesor =
//        profesorRepository.findById(id).map {
//            it.nombreCompleto = profesorNuevo.nombreCompleto
//            it.materias = profesorNuevo.materias
//            profesorRepository.save(it)
//            it
//        }.orElseThrow {
//            ResponseStatusException(HttpStatus.NOT_FOUND, "El profesor con identificador $id no existe")
//        }

}