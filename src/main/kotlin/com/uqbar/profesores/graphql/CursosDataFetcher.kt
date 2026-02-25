package com.uqbar.profesores.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.uqbar.profesores.domain.Curso
import com.uqbar.profesores.domain.Profesor
import graphql.schema.DataFetchingEnvironment
import org.dataloader.DataLoader
import java.util.concurrent.CompletionStage

@DgsComponent
class CursosDataFetcher {
    @DgsData(parentType = "Profesor", field = "cursos")
    fun cursos(dataFetchingEnvironment: DataFetchingEnvironment): CompletionStage<List<Curso>> {
        // 1. Obtenemos el objeto Profesor padre
        val profesor = dataFetchingEnvironment.getSource<Profesor>()
        // 2. Obtenemos el DataLoader configurado para devolver List<Curso> por ID de Profesor
        // La clave es que el Data Loader ahora mapea Long (ID Profesor) a List<Curso>
        val dataLoader: DataLoader<Long, List<Curso>> = dataFetchingEnvironment.getDataLoader("cursos")!!

        // 3. Cargamos la lista de cursos usando el ID del profesor
        return dataLoader.load(profesor?.id!!)
    }
}