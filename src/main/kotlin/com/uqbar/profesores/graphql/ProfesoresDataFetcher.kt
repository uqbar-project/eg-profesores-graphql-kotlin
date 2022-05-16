package com.uqbar.profesores.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsEnableDataFetcherInstrumentation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import com.uqbar.profesores.service.ProfesorService
import org.springframework.beans.factory.annotation.Autowired

@DgsComponent
class ProfesoresDataFetcher {

   @Autowired
   lateinit var profesorService: ProfesorService

   @DgsQuery
   fun profesores(@InputArgument nombreFilter: String?) =
      profesorService.getProfesoresByNombre((nombreFilter ?: "") + "%")

   @DgsQuery
   @DgsEnableDataFetcherInstrumentation(false)
   fun profesor(@InputArgument idProfesor: Int) =
      profesorService.getProfesor(idProfesor.toLong())
}