package com.uqbar.profesores

import com.uqbar.profesores.domain.MateriaDesafiante
import com.uqbar.profesores.domain.MateriaInteresante
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.net.URI

class MateriaInteresanteTest {

    private fun getMateriaInteresantePocoGradoInteres() = MateriaInteresante().apply {
        gradoDeInteres = 50
        codigo = "2014"
        sitioWeb = URI.create("http://lamateria.edu.ar").toURL()
        cargaHoraSemanal = 100.0
        anio = 2
        nombre = "Materiota"
    }

    @Test
    @DisplayName("Una materia interesante con un grado de interés bajo tiene una carga total menor")
    fun materiaMenosInteresanteCargaTotal() {
        val materiaInteresante = getMateriaInteresantePocoGradoInteres()
        Assertions.assertEquals(110.0, materiaInteresante.cargaTotal())
    }

    @Test
    @DisplayName("Una materia interesante con un grado de interés alto tiene una carga total mayor")
    fun materiaMasInteresanteCargaTotal() {
        val materiaInteresante = getMateriaInteresantePocoGradoInteres().apply {
            gradoDeInteres = 51
        }
        Assertions.assertEquals(120.0, materiaInteresante.cargaTotal())
    }

}

class MateriaDesafianteTest {

    private fun getMateriaDesafiantePocoGradoInteres() = MateriaDesafiante().apply {
        codigo = "2014"
        sitioWeb = URI.create("http://lamateria.edu.ar").toURL()
        cargaHoraSemanal = 150.0
        anio = 2
        nombre = "Materiota"
        cargaHorasExtra = 50.0
        momentoDificil = false
    }

    @Test
    @DisplayName("Una materia desafiante en un momento fácil tiene una carga total menor")
    fun materiaDesafianteNoDificilCargaTotal() {
        val materiaDesafiante = getMateriaDesafiantePocoGradoInteres()
        Assertions.assertEquals(160.0, materiaDesafiante.cargaTotal())
    }

    @Test
    @DisplayName("Una materia desafiante en un momento difícil tiene una carga total mayor")
    fun materiaDesafianteDificilCargaTotal() {
        val materiaInteresante = getMateriaDesafiantePocoGradoInteres().apply {
            momentoDificil = true
        }
        Assertions.assertEquals(200.0, materiaInteresante.cargaTotal())
    }

}