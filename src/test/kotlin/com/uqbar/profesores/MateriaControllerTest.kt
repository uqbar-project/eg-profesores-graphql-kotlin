package com.uqbar.profesores

import com.uqbar.profesores.domain.Materia
import com.uqbar.profesores.repos.MateriaRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Dado un controller de materias")
class MateriaControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var repoMaterias: MateriaRepository

    @Test
    fun `obtener todas las materias`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/materias"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
    }

    @Test
    fun `al buscar la informacion de una materia que no existe recibimos como respuesta un codigo de http 404`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/materias/124124125125"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `al buscar la informacion de una materia que no tiene docentes recibimos una lista vacia de profesores que la dan`() {
        val materiaSinProfesores = repoMaterias.save(Materia(nombre = "Geometría Analítica", anio = 1))

        mockMvc.perform(MockMvcRequestBuilders.get("/materias/${materiaSinProfesores.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value(materiaSinProfesores.nombre))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(materiaSinProfesores.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.profesores").isEmpty)
    }

    @Test
    fun `al buscar la informacion de una materia correcta recibimos la agrupacion de profesores que la dan`() {
        val materia = repoMaterias.findAll().first()
        mockMvc.perform(MockMvcRequestBuilders.get("/materias/${materia.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value(materia.nombre))
            .andExpect(MockMvcResultMatchers.jsonPath("$.anio").value(materia.anio))
            .andExpect(MockMvcResultMatchers.jsonPath("$.profesores").isArray)
    }


}