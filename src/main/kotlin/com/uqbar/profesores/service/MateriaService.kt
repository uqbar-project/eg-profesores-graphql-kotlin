package com.uqbar.profesores.service

import com.uqbar.profesores.errorHandling.NotFoundException
import com.uqbar.profesores.repos.MateriaRepository
import com.uqbar.profesores.serializer.MateriaDTO
import com.uqbar.profesores.serializer.ProfesorDTO
import org.hibernate.annotations.NotFound
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MateriaService {

    @Autowired
    lateinit var materiaRepository: MateriaRepository

    @Transactional(readOnly = true)
    fun getMaterias() = materiaRepository.findAll()

    @Transactional(readOnly = true)
    fun getMateria(id: Long): MateriaDTO {
        // Validación de que la materia existe
        val materiaOriginal = materiaRepository.findById(id)
            .orElseThrow { NotFoundException("La materia con identificador $id no existe") }

        // Recibimos n registros de materias
        val materiasDTO = materiaRepository.findFullById(id)
        if (materiasDTO.isEmpty()) {
            return MateriaDTO(materiaOriginal.id, materiaOriginal.nombre, materiaOriginal.anio, listOf())
        }

        // Agrupamos los profesores de la materia
        val materia = materiasDTO.first()
        val profesores = materiasDTO.map { ProfesorDTO(it.getProfesorId(), it.getProfesorNombre()) }
        return MateriaDTO(materia.getId(), materia.getNombreLindo(), materia.getAnio(), profesores)
    }

}