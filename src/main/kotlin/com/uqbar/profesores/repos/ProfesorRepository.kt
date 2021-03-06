package com.uqbar.profesores.repos

import com.uqbar.profesores.domain.Profesor
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.*

interface ProfesorRepository : CrudRepository<Profesor, Long> {
    @EntityGraph(attributePaths = ["materias"])
    override fun findById(id: Long): Optional<Profesor>

    @EntityGraph(attributePaths = ["materias"])
    @Query("select p from Profesor as p where p.apellido like :nombreFilter or p.nombre like :nombreFilter")
    fun findAllByNombreOrApellido(@Param("nombreFilter") nombreFilter: String): List<Profesor>
}