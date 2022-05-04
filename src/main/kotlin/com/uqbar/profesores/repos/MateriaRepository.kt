package com.uqbar.profesores.repos

import com.uqbar.profesores.domain.Materia
import com.uqbar.profesores.serializer.MateriaFullRowDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface MateriaRepository : CrudRepository<Materia, Long> {
    @Query("""
        SELECT m.id as id,
               m.nombre as nombre, 
               m.anio as anio,
               p.id as profesorId,
               p.nombreCompleto as profesorNombre 
          FROM Profesor p 
               INNER JOIN p.materias m
         WHERE m.id = :id
        """)
    fun findFullById(id: Long): List<MateriaFullRowDTO>

    fun findByNombre(nombreMateria: String): List<Materia>
}