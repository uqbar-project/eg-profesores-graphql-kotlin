package com.uqbar.profesores

import com.uqbar.profesores.domain.Materia
import com.uqbar.profesores.domain.Profesor
import com.uqbar.profesores.repos.MateriaRepository
import com.uqbar.profesores.repos.ProfesorRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.net.URL

@DataJpaTest
class MateriaRepositoryTest {
   @Autowired
   lateinit var materiaRepository: MateriaRepository

   @Autowired
   lateinit var profesorRepository: ProfesorRepository

   @Test
   fun `al buscar una materia que no tiene profesores no trae nada`() {
      // Arrange
      val materia = materiaRepository.save(Materia(nombre = "Geometría Analítica", anio = 1, codigo = "TI05", sitioWeb = URL("http://geometr.ia.com.ar"), cargaHoraSemanal = 6))

      // Act
      val materias = materiaRepository.findFullById(materia.id)

      // Assert
      assertTrue(materias.isEmpty())
   }

   @Test
   fun `al buscar una materia que tiene varios profesores trae una fila por cada profesor (producto cartesiano)`() {
      // Arrange
      val materia = materiaRepository.save(Materia(nombre = "Algoritmos II", anio = 1, codigo = "TPI10", sitioWeb = URL("https://algo2.uqbar-project.org"), cargaHoraSemanal = 6))
      profesorRepository.save(Profesor(nombre = "Fernando", apellido = "Dodino", anioComienzo = 1997, puntajeDocente = 90).apply {
         materias = mutableSetOf(materia)
      })
      profesorRepository.save(Profesor(nombre = "Julián", apellido = "Mosquera", anioComienzo =  2016, puntajeDocente = 100).apply {
         materias = mutableSetOf(materia)
      })

      // Act
      val materias = materiaRepository.findFullById(materia.id)

      // Assert
      assertEquals(2, materias.size)
   }

   /**
    * Este test prueba la independencia de la información generada en cada test
    * donde al final de la ejecución de cada test se rollbackea cualquier actualización.
    * Esto permite que sean independientes y que no importe el orden en el que se evalúan.
    */
   @Test
   fun `al buscar materias al repositorio solo trae la informacion generada para ese test`() {
      // Arrange
      materiaRepository.save(Materia(nombre = "Algoritmos I", anio = 1, codigo = "TI07", sitioWeb = URL("http://algo1.unsam.edu.ar"), cargaHoraSemanal = 5))

      // Assert
      assertEquals(materiaRepository.findAll().count(), 1)
   }

}