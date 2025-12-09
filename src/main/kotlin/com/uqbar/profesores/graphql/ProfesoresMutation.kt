package com.uqbar.profesores.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.uqbar.profesores.domain.Materia
import com.uqbar.profesores.domain.MateriaInteresante
import com.uqbar.profesores.domain.Profesor
import com.uqbar.profesores.service.ProfesorService
import org.springframework.beans.factory.annotation.Autowired
import java.net.URI

@DgsComponent
class ProfesoresMutation {

   @Autowired
   lateinit var profesorService: ProfesorService

   @DgsMutation
   fun actualizarProfesor(updateProfesor: UpdateProfesor) =
      profesorService.actualizarProfesor(updateProfesor.id.toLong(), updateProfesor.toProfesor())

   @DgsMutation
   fun agregarMateria(idProfesor: Int, materiaInput: MateriaInput) =
      profesorService.agregarMateria(idProfesor.toLong(), materiaInput.toMateria())

}

data class UpdateProfesor(val id: Int, val nombre: String, val apellido: String) {
   fun toProfesor() = Profesor(nombre = nombre, apellido = apellido)
}

data class MateriaInput(val id: Int, val _nombre: String) {
   fun toMateria(): Materia {
      val sitioMateria = _nombre.replace(" ", "").lowercase()
      val materia = MateriaInteresante().apply {
         nombre = _nombre
         sitioWeb = URI.create("https://facultad.edu.ar/$sitioMateria").toURL()
      }
      materia.id = id.toLong()
      return materia
   }
}