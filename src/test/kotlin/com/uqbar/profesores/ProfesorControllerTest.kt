package com.uqbar.profesores

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.uqbar.profesores.domain.Materia
import com.uqbar.profesores.domain.Profesor
import com.uqbar.profesores.repos.MateriaRepository
import com.uqbar.profesores.repos.ProfesorRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Dado un controller de profesores")
class ProfesorControllerTest {
    private val mapper = jacksonObjectMapper()

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var repoProfes: ProfesorRepository

    @Autowired
    lateinit var repoMaterias: MateriaRepository

    @Test
    fun `al consultar todos los profesores no sabemos las materias en las que participa`() {
        val responseEntity = mockMvc
            .perform(MockMvcRequestBuilders.get("/profesores"))
            .andReturn().response
        val profesores = mapper.readValue<List<Profesor>>(responseEntity.contentAsString)
        assertEquals(200, responseEntity.status)
        assertTrue(profesores.isNotEmpty())
        // los profesores no traen las materias
        assertEquals(0, profesores.first().materias.size)
    }

    @Test
    fun `al traer el dato de un profesor trae las materias en las que participa`() {
        val profesorId = crearProfesorConMaterias()

        val profesorPrueba = getProfesor(profesorId)

        val responseEntity = mockMvc.perform(MockMvcRequestBuilders.get("/profesores/${profesorPrueba.id}")).andReturn().response
        assertEquals(200, responseEntity.status)
        val profesor = mapper.readValue<Profesor>(responseEntity.contentAsString)
        assertEquals(profesorPrueba.materias.size, profesor.materias.size)
    }

    @Test
    fun `no podemos traer informacion de un profesor inexistente`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/profesores/10000"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    @Transactional
    fun `podemos actualizar la informacion de un profesor`() {
        // Arrange
        val profesorId = crearProfesorConMaterias()
        val profesorOriginal = getProfesor(profesorId)
        val materiaNueva = repoMaterias.save(Materia().apply {
            nombre = "Ingeniería de Software"
        })
        val cantidadMateriasOriginales = profesorOriginal.materias.size
        assertEquals(1, cantidadMateriasOriginales)

        // Act
        profesorOriginal.agregarMateria(materiaNueva)
        updateProfesor(profesorOriginal)

        // Assert
        val nuevoProfesor = getProfesor(profesorOriginal.id)
        val materiasDelProfesorAhora = nuevoProfesor.materias.size
        assertEquals(materiasDelProfesorAhora, cantidadMateriasOriginales + 1)
    }

    @Test
    fun `no podemos actualizar un profesor inexistente`() {
        val idInexistente = 10022L
        val profesorBody = mapper.writeValueAsString(Profesor().apply {
            nombreCompleto = "Juan Contardo"
            id = idInexistente
        })
        mockMvc.perform(MockMvcRequestBuilders.put("/profesores/$idInexistente")
            .contentType("application/json")
            .content(profesorBody)
        ).andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    private fun updateProfesor(profesor: Profesor) {
        val profesorBody = mapper.writeValueAsString(profesor)
        mockMvc.perform(MockMvcRequestBuilders.put("/profesores/${profesor.id}")
                .contentType("application/json")
                .content(profesorBody)
        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    private fun getProfesor(idProfesor: Long): Profesor =
        repoProfes.findById(idProfesor).orElseThrow { RuntimeException("Profesor con identificador $idProfesor no existe") }

    private fun crearProfesorConMaterias(): Long {
        val materia = Materia().apply {
            anio = 2
            nombre = "Algoritmos I"
        }
        repoMaterias.save(materia)
        val profesor = repoProfes.save(Profesor().apply {
            nombreCompleto = "Juana Fischetti"
            materias = mutableSetOf(materia)
        })
        return profesor.id
    }


}