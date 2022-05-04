package com.uqbar.profesores

import com.uqbar.profesores.controller.MateriaController
import com.uqbar.profesores.errorHandling.NotFoundException
import com.uqbar.profesores.service.MateriaService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * Estos tests son de ejemplo de @WebMvcTest, que mockea la respuesta
 */
@WebMvcTest(controllers = [MateriaController::class])
class MateriaControllerWebMvcTest {
   @MockBean
   lateinit var materiaService: MateriaService

   @Autowired
   lateinit var mockMvc: MockMvc

   @Test
   fun `al pedir la informacion completa de una materia que no existe tiene que devolver un 404`() {
      Mockito.`when`(materiaService.getMateria(1)).thenAnswer { throw NotFoundException("Materia no existe") }
      mockMvc.perform(MockMvcRequestBuilders.get("/materias/1"))
         .andExpect(MockMvcResultMatchers.status().isNotFound)
   }
}
