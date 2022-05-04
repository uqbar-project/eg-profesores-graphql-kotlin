package com.uqbar.profesores.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import com.uqbar.profesores.domain.Profesor
import com.uqbar.profesores.repos.ProfesorRepository
import org.springframework.beans.factory.annotation.Autowired

@DgsComponent
class ProfesoresDataFetcher {

   @Autowired
   lateinit var profesorRepository: ProfesorRepository

   @DgsQuery
   fun profesores(@InputArgument nombreFilter : String?) =
      profesorRepository.findAllByNombreCompleto((nombreFilter ?: "") + "%")

}