package com.uqbar.profesores

import com.uqbar.profesores.domain.MateriaDesafiante
import com.uqbar.profesores.domain.MateriaInteresante
import com.uqbar.profesores.domain.Profesor
import com.uqbar.profesores.repos.MateriaRepository
import com.uqbar.profesores.repos.ProfesorRepository
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.URI

/**
 *
 * Para explorar otras opciones
 * https://stackoverflow.com/questions/38040572/spring-boot-loading-initial-data
 */
@Service
class ProfesoresBootstrap : InitializingBean {
    @Autowired
    lateinit var repoMaterias: MateriaRepository

    @Autowired
    lateinit var repoProfes : ProfesorRepository

    override fun afterPropertiesSet() {
        println("************************************************************************")
        println("Running initialization")
        println("************************************************************************")
        init()
    }

    fun init() {
        val	algoritmos = MateriaInteresante().apply {
            nombre = "Algoritmos y Estructura de Datos"
            anio = 1
            codigo = "08-2021"
            sitioWeb = URI.create("https://sites.google.com/site/algoritmosutnfrba/home").toURL()
            cargaHoraSemanal = 5.0
            gradoDeInteres = 40

        }
        val paradigmas = MateriaDesafiante().apply {
            nombre = "Paradigmas de Programacion"
            anio = 2
            codigo = "08-2026"
            sitioWeb = URI.create("https://pdep.com.ar").toURL()
            cargaHoraSemanal = 8.0
            cargaHorasExtra = 10.0
            momentoDificil = false
        }
        val disenio = MateriaInteresante().apply {
            nombre = "Diseño de Sistemas"
            anio = 3
            codigo = "08-2028"
            sitioWeb = URI.create("https://dds-jv.github.io/apuntes/modelado-objetos/").toURL()
            cargaHoraSemanal = 6.0
            gradoDeInteres = 60
        }

        repoMaterias.save(algoritmos)
        repoMaterias.save(paradigmas)
        repoMaterias.save(disenio)
        repoMaterias.save(MateriaDesafiante().apply {
            nombre = "Sistemas Operativos"
            anio = 2
            codigo = "08-2027"
            sitioWeb = URI.create("https://www.utnso.com.ar/").toURL()
            cargaHoraSemanal = 8.0
            cargaHorasExtra = 25.0
            momentoDificil = true
        })
        val spigariol = Profesor(nombre = "Lucas", apellido = "Spigariol", anioComienzo = 1995, puntajeDocente = 91)
        spigariol.agregarMateria(algoritmos)
        spigariol.agregarMateria(paradigmas)

        val passerini = Profesor(nombre = "Nicolás", apellido = "Passerini", anioComienzo =  2002, puntajeDocente = 84)
        passerini.agregarMateria(paradigmas)
        passerini.agregarMateria(disenio)

        val dodino = Profesor(nombre = "Fernando", apellido = "Dodino", anioComienzo = 1997, puntajeDocente = 90)
        dodino.agregarMateria(disenio)

        repoProfes.save(spigariol)
        repoProfes.save(passerini)
        repoProfes.save(dodino)

    }
}