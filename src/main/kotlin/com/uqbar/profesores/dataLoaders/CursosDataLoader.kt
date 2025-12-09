package com.uqbar.profesores.dataLoaders

import com.netflix.graphql.dgs.DgsDataLoader
import com.uqbar.profesores.domain.Curso
import com.uqbar.profesores.service.CursoService
import org.dataloader.BatchLoader
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.concurrent.Executors

/**
 * Data Loader para cargar los cursos de múltiples profesores en un solo batch.
 *
 * KEY: Long (ID del Profesor)
 * VALUE: List<Curso> (Los cursos de ese profesor)
 */
@DgsDataLoader(name = "cursos")
class CursosDataLoader : BatchLoader<Long, List<Curso>> {
    // Thread pool dedicado para evitar quedarnos bloqueados
    // https://netflix.github.io/dgs/data-loaders/#thread-pool-optimization
    private val ioExecutor = Executors.newCachedThreadPool()

    @Autowired
    lateinit var cursoService: CursoService

    override fun load(idProfesores: MutableList<Long>): CompletionStage<List<List<Curso>>> {
        return CompletableFuture.supplyAsync({
            // Llama al servicio para obtener los cursos de todos los profesores en la lista.
            // Hay que devolver una lista de listas de cursos, donde la posición de la lista interna
            // corresponde al id de profesor en la misma posición.
            cursoService.getCursosPorProfesor(idProfesores)
        }, ioExecutor)
    }
}