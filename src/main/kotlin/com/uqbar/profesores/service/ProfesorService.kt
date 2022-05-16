package com.uqbar.profesores.service

import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException
import com.uqbar.profesores.domain.Materia
import com.uqbar.profesores.domain.Profesor
import com.uqbar.profesores.repos.MateriaRepository
import com.uqbar.profesores.repos.ProfesorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable

@Service
class ProfesorService {
    @Autowired
    lateinit var profesorRepository: ProfesorRepository

    @Autowired
    lateinit var materiaRepository: MateriaRepository

    @Transactional(readOnly = true)
    fun getProfesoresByNombre(nombreFilter: String) = this.profesorRepository.findAllByNombreOrApellido(nombreFilter)

    @Transactional(readOnly = true)
    fun getProfesor(@PathVariable id: Long) =
        this.profesorRepository.findById(id).orElseThrow {
            DgsEntityNotFoundException("El profesor con identificador $id no existe")
        }

    @Transactional
    fun actualizarProfesor(idProfesor: Long, profesorActualizado: Profesor): Profesor =
        profesorRepository.findById(idProfesor).map {
            it.nombre = profesorActualizado.nombre
            it.apellido = profesorActualizado.apellido
            profesorRepository.save(it)
            it
        }.orElseThrow {
            DgsEntityNotFoundException("El profesor con identificador $idProfesor no existe")
        }

    @Transactional
    fun agregarMateria(idProfesor: Long, materia: Materia): Profesor {
        val profesor = profesorRepository.findById(idProfesor).orElseThrow {
            DgsEntityNotFoundException("El profesor con identificador $idProfesor no existe")
        }
        println("eeeh? " + materia.nombre.isEmpty())
        val materiaNueva = if (materia.nombre.isEmpty()) {
            println("busco por id " + materia.id)
            materiaRepository.findById(materia.id)
        } else {
            println("busco por nombre")
            materiaRepository.findByNombre(materia.nombre)
        }
        if (!materiaNueva.isPresent) throw DgsEntityNotFoundException("La materia no existe")
        profesor.agregarMateria(materiaNueva.get())
        profesorRepository.save(profesor)
        return profesor
    }

}