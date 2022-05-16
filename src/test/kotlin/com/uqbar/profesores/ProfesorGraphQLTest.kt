package com.uqbar.profesores

import com.jayway.jsonpath.TypeRef
import com.netflix.graphql.dgs.DgsQueryExecutor
import com.uqbar.profesores.domain.Materia
import com.uqbar.profesores.domain.Profesor
import com.uqbar.profesores.repos.MateriaRepository
import com.uqbar.profesores.repos.ProfesorRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.net.URL

@SpringBootTest
@ActiveProfiles("test")
class ProfesorGraphQLTest {
    @Autowired
    lateinit var repoProfes: ProfesorRepository

    @Autowired
    lateinit var repoMaterias: MateriaRepository

    @Autowired
    lateinit var dgsQueryExecutor: DgsQueryExecutor

    @Test
    fun `consulta de un profesor con sus materias`() {
        // Arrange
        val profesorId = crearProfesorConMaterias()
        val profesorPrueba = getProfesor(profesorId)

        // Act
        val nombreABuscar = profesorPrueba.apellido.take(3)

        // Assert
        val profesoresResult = buscarProfesores(nombreABuscar)
        val profesorResult = profesoresResult.first()
        Assertions.assertThat(profesorResult.nombre).isEqualTo(profesorPrueba.nombre)
        Assertions.assertThat(profesorResult.materias.first().nombre).contains(profesorPrueba.materias.first().nombre)
    }

    @Test
    fun `consulta de profesores por un nombre inexistente no trae informacion`() {
        val profesoresResult = buscarProfesores("ZZZZZZ")
        Assertions.assertThat(profesoresResult).isEmpty()
    }

    @Test
    fun `consulta de un profesor trae los datos correctamente`() {
        // Arrange
        val profesorId = crearProfesorConMaterias()
        val profesorPrueba = getProfesor(profesorId)

        // Act
        val profesorResult = buscarProfesor(profesorId)

        // Assert
        Assertions.assertThat(profesorResult.nombre).isEqualTo(profesorPrueba.nombre)
        Assertions.assertThat(profesorResult.materias.first().sitioWeb.toString()).contains(profesorPrueba.materias.first().sitioWeb.toString())
    }

//    @Test
//    @Transactional
//    fun `podemos actualizar la informacion de un profesor`() {
//        // Arrange
//        val profesorId = crearProfesorConMaterias()
//        val profesorOriginal = getProfesor(profesorId)
//        val materiaNueva = repoMaterias.save(Materia(nombre = "Ingeniería de Software", sitioWeb = URL("http://ingenieria-software.edu.ar"), cargaHoraSemanal = 5, anio = 5, codigo = "TPI25"))
//        val cantidadMateriasOriginales = profesorOriginal.materias.size
//        assertEquals(1, cantidadMateriasOriginales)
//
//        // Act
//        profesorOriginal.agregarMateria(materiaNueva)
//        updateProfesor(profesorOriginal)
//
//        // Assert
//        val nuevoProfesor = getProfesor(profesorOriginal.id)
//        val materiasDelProfesorAhora = nuevoProfesor.materias.size
//        assertEquals(materiasDelProfesorAhora, cantidadMateriasOriginales + 1)
//    }

//    @Test
//    fun `no podemos actualizar un profesor inexistente`() {
//        val idInexistente = 10022L
//        val profesorBody = mapper.writeValueAsString(Profesor(nombre = "Juan", apellido = "Contardo", anioComienzo = 2013, puntajeDocente = 95))
//        mockMvc.perform(MockMvcRequestBuilders.put("/profesores/$idInexistente")
//            .contentType("application/json")
//            .content(profesorBody)
//        ).andExpect(MockMvcResultMatchers.status().isNotFound)
//    }

    private fun buscarProfesor(idProfesor: Long) = dgsQueryExecutor.executeAndExtractJsonPathAsObject("""
                {
                    profesor(idProfesor: $idProfesor) {
                        nombre
                        apellido
                        materias {
                            nombre
                            sitioWeb
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
                            nombre
                            codigo
                        }
                    }
                }
            """.trimIndent(), "data.profesores[*]", object : TypeRef<List<Profesor>>() {}
    )

    private fun getProfesor(idProfesor: Long): Profesor =
        repoProfes.findById(idProfesor).orElseThrow { RuntimeException("Profesor con identificador $idProfesor no existe") }

    private fun crearProfesorConMaterias(): Long {
        val materia = Materia(nombre = "Algoritmos I", anio = 1, codigo = "TI07", sitioWeb = URL("http://algo1.unsam.edu.ar"), cargaHoraSemanal = 5)
        repoMaterias.save(materia)
        val profesor = repoProfes.save(Profesor(nombre = "Juana", apellido = "Fischetti", puntajeDocente = 56, anioComienzo = 2019).apply {
            materias = mutableSetOf(materia)
        })
        return profesor.id
    }


}