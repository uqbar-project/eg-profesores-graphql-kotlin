package com.uqbar.profesores.service

import com.uqbar.profesores.domain.Curso
import com.uqbar.profesores.repos.ProfesorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CursoService {

    @Autowired
    private lateinit var profesorRepository: ProfesorRepository

    @Transactional(readOnly = true)
    fun getCursosPorProfesor(idProfesores: List<Long?>): List<List<Curso>> {
        // 1. Obtener todos los cursos para todos los IDs de profesores en una sola consulta.
        // Se asume que la entidad Curso tiene una referencia directa al Profesor (curso.profesor.id).
        val profesoresConCursos = profesorRepository.findAllById(idProfesores.filterNotNull())

        // 2. Agrupar los cursos por el ID del profesor para facilitar la búsqueda.
        // Convertimos la lista de profesores encontrados en un mapa.
        val cursosAgrupadosPorProfesorId: Map<Long, List<Curso>> = profesoresConCursos
            .associate { profesor ->
                // La clave es el ID del profesor. El valor es la lista de sus cursos.
                // Convertimos el MutableSet<Curso> a List<Curso>.
                profesor.id to profesor.cursos.toList()
            }

        // 3. Mapear la lista de IDs de entrada al resultado agrupado, MANTENIENDO EL ORDEN.
        return idProfesores.map { profesorId ->
            // getOrElse es importante: si un profesor existe pero no tiene cursos, devuelve una lista vacía
            // asegurando que la lista externa SIEMPRE tenga el mismo tamaño
            // que la lista de entrada (profesorIds).
            cursosAgrupadosPorProfesorId.getOrElse(profesorId!!) { emptyList() }
        }
    }

}