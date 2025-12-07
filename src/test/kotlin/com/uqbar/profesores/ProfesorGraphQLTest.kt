package com.uqbar.profesores

import com.jayway.jsonpath.TypeRef
import com.netflix.graphql.dgs.DgsQueryExecutor
import com.uqbar.profesores.domain.MateriaDesafiante
import com.uqbar.profesores.domain.MateriaInteresante
import com.uqbar.profesores.domain.Profesor
import com.uqbar.profesores.graphql.MateriaInput
import com.uqbar.profesores.graphql.ProfesoresMutation
import com.uqbar.profesores.graphql.UpdateProfesor
import com.uqbar.profesores.repos.MateriaRepository
import com.uqbar.profesores.repos.ProfesorRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.net.URI

@SpringBootTest
@ActiveProfiles("test")
class ProfesorGraphQLTest {
   @Autowired
   lateinit var repoProfes: ProfesorRepository

   @Autowired
   lateinit var repoMaterias: MateriaRepository

   @Autowired
   lateinit var dgsQueryExecutor: DgsQueryExecutor

   @Autowired
   lateinit var profesoresMutation: ProfesoresMutation

   @Test
   fun `consulta de un profesor con sus materias`() {
      // Arrange
      val profesorPrueba = crearProfesorConMaterias()

      // Act
      val nombreABuscar = profesorPrueba.apellido.take(3)

      // Assert
      val profesoresResult = buscarProfesores(nombreABuscar)
      val profesorResult = profesoresResult.first()
      Assertions.assertThat(profesorResult.nombre).isEqualTo(profesorPrueba.nombre)
      Assertions.assertThat(profesorResult.materias.map { it.nombre }).containsExactlyInAnyOrderElementsOf(profesorPrueba.materias.map { it.nombre })
   }

   @Test
   fun `consulta de profesores por un nombre inexistente no trae informacion`() {
      val profesoresResult = buscarProfesores("ZZZZZZ")
      Assertions.assertThat(profesoresResult).isEmpty()
   }

   @Test
   fun `consulta de un profesor trae los datos correctamente`() {
      // Arrange
      val profesorPrueba = crearProfesorConMaterias()

      // Act
      val profesorResult = buscarProfesor(profesorPrueba.id)

      // Assert
      Assertions.assertThat(profesorResult.nombre).isEqualTo(profesorPrueba.nombre)
      Assertions.assertThat(profesorResult.materias.map { it.nombre }).containsExactlyInAnyOrderElementsOf(profesorPrueba.materias.map { it.nombre })
   }

   @Test
   @Transactional
   fun `podemos agregar una materia a un profesor por id de materia`() {
      // Arrange
      val profesorOriginal = crearProfesorConMaterias()

      val materiaNueva = repoMaterias.save(materiaDesafiante())
      val cantidadMateriasOriginales = profesorOriginal.materias.size
      Assertions.assertThat(1).isEqualTo(cantidadMateriasOriginales)

      // Act
      profesorOriginal.agregarMateria(materiaNueva)
      profesoresMutation.agregarMateria(profesorOriginal.id.toInt(), MateriaInput(materiaNueva.id.toInt(), ""))

      // Assert
      val nuevoProfesor = getProfesor(profesorOriginal.id)
      val materiasDelProfesorAhora = nuevoProfesor.materias.size
      Assertions.assertThat(materiasDelProfesorAhora).isEqualTo(cantidadMateriasOriginales + 1)
   }

   @Test
   @Transactional
   fun `podemos agregar una materia a un profesor por nombre de materia`() {
      // Arrange
      val profesorOriginal = crearProfesorConMaterias()
      val materiaNueva = repoMaterias.save(materiaDesafiante())
      val cantidadMateriasOriginales = profesorOriginal.materias.size
      Assertions.assertThat(1).isEqualTo(cantidadMateriasOriginales)

      // Act
      profesorOriginal.agregarMateria(materiaNueva)
      profesoresMutation.agregarMateria(profesorOriginal.id.toInt(), MateriaInput(0, materiaNueva.nombre))

      // Assert
      val nuevoProfesor = getProfesor(profesorOriginal.id)
      val materiasDelProfesorAhora = nuevoProfesor.materias.size
      Assertions.assertThat(materiasDelProfesorAhora).isEqualTo(cantidadMateriasOriginales + 1)
   }

   @Test
   @Transactional
   fun `podemos actualizar los datos de un profesor`() {
      // Arrange
      val profesorOriginal = crearProfesorConMaterias()

      // Act
      profesoresMutation.actualizarProfesor(
         UpdateProfesor(
            id = profesorOriginal.id.toInt(),
            nombre = "Ferenc",
            apellido = "Puszkas"
         )
      )

      // Assert
      val nuevoProfesor = getProfesor(profesorOriginal.id)
      Assertions.assertThat(nuevoProfesor.nombre).isEqualTo("Ferenc")
      Assertions.assertThat(nuevoProfesor.apellido).isEqualTo("Puszkas")
      Assertions.assertThat(nuevoProfesor.puntajeDocente).isEqualTo(profesorOriginal.puntajeDocente)
   }

   /************************************************************************************************
    *
    */

   private fun buscarProfesor(idProfesor: Long) = dgsQueryExecutor.executeAndExtractJsonPathAsObject("""
                {
                    profesor(idProfesor: $idProfesor) {
                        nombre
                        apellido
                        materias {
                            __typename
                            nombre
                            sitioWeb
                            ... on MateriaInteresante {
                                 gradoDeInteres
                             }
                             ... on MateriaDesafiante {
                                 cargaHorasExtra
                                 momentoDificil
                             }
                        }
                    }
                }
            """.trimIndent(), "data.profesor", object : TypeRef<Profesor>() {}
   )

   // Ojo que definir atributos no-nullables y sin defaults puede romper los tests!!
   // Por ejemplo, si definimos anio no nulo y sin un valor por defecto, la deserialización se rompe
   private fun buscarProfesores(nombreABuscar: String) = dgsQueryExecutor.executeAndExtractJsonPathAsObject("""
                {
                    profesores(nombreFilter: "$nombreABuscar") {
                        nombre
                        apellido
                        materias {
                            __typename
                            nombre
                            codigo
                             ... on MateriaInteresante {
                                 gradoDeInteres
                             }
                             ... on MateriaDesafiante {
                                 cargaHorasExtra
                                 momentoDificil
                             }
                        }
                    }
                }
            """.trimIndent(), "data.profesores[*]", object : TypeRef<List<Profesor>>() {}
   )

   private fun getProfesor(idProfesor: Long): Profesor =
      repoProfes.findById(idProfesor)
         .orElseThrow { RuntimeException("Profesor con identificador $idProfesor no existe") }

   private fun crearProfesorConMaterias(): Profesor {
      val materiaInteresante = MateriaInteresante().apply {
         nombre = "Algoritmos I"
         anio = 1
         codigo = "TI07"
         sitioWeb = URI.create("http://algo1.unsam.edu.ar").toURL()
         cargaHoraSemanal = 5.0
         gradoDeInteres = 40
      }
      repoMaterias.save(materiaInteresante)
      val profesor = repoProfes.save(
         Profesor(
            nombre = "Juana",
            apellido = "Fischetti",
            puntajeDocente = 56,
            anioComienzo = 2019
         ).apply {
            materias = mutableSetOf(materiaInteresante)
         })
      return profesor
   }

   private fun materiaDesafiante(): MateriaDesafiante = MateriaDesafiante().apply {
      nombre = "Ingenieria de Software"
      sitioWeb = URI.create("http://ingenieria-software.edu.ar").toURL()
      cargaHoraSemanal = 5.0
      anio = 5
      codigo = "TPI25"
      cargaHorasExtra = 3.0
      momentoDificil = false
   }
}