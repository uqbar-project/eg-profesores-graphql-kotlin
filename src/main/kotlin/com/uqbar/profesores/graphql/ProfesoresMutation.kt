package com.uqbar.profesores.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.uqbar.profesores.domain.Materia
import com.uqbar.profesores.domain.Profesor
import com.uqbar.profesores.service.ProfesorService
import org.springframework.beans.factory.annotation.Autowired

@DgsComponent
class ProfesoresMutation {

   @Autowired
   lateinit var profesorService: ProfesorService

   @DgsData(parentType = "Mutation")
   fun actualizarProfesor(updateProfesor: UpdateProfesor) =
      profesorService.actualizarProfesor(updateProfesor.id.toLong(), updateProfesor.toProfesor())

   @DgsData(parentType = "Mutation")
   fun agregarMateria(idProfesor: Int, materiaInput: MateriaInput) =
      profesorService.agregarMateria(idProfesor.toLong(), materiaInput.toMateria())

}

data class UpdateProfesor(val id: Int, val nombre: String, val apellido: String) {
   fun toProfesor() = Profesor(nombre = nombre, apellido = apellido)
}

data class MateriaInput(val id: Int, val nombre: String) {
   fun toMateria(): Materia {
      val materia = Materia(nombre = nombre, sitioWeb = null)
      materia.id = id.toLong()
      return materia
   }
}