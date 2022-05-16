package com.uqbar.profesores.repos

import com.uqbar.profesores.domain.Materia
import org.springframework.data.repository.CrudRepository
import java.util.*

interface MateriaRepository : CrudRepository<Materia, Long> {
    fun findByNombre(nombre: String): Optional<Materia>
}