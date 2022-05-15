package com.uqbar.profesores

import com.uqbar.profesores.domain.Materia
import com.uqbar.profesores.domain.Profesor
import com.uqbar.profesores.repos.MateriaRepository
import com.uqbar.profesores.repos.ProfesorRepository
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.URL

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
        val	algoritmos = Materia(nombre = "Algoritmos y Estructura de Datos", anio = 1, codigo = "08-2021", sitioWeb = URL("https://sites.google.com/site/algoritmosutnfrba/home"), cargaHoraSemanal = 5)
        val paradigmas = Materia(nombre = "Paradigmas de Programacion", anio = 2, codigo = "08-2026", sitioWeb = URL("https://pdep.com.ar"), cargaHoraSemanal = 8)
        val disenio = Materia(nombre = "Diseño de Sistemas", anio = 3, codigo = "08-2028", sitioWeb = URL("https://dds-jv.github.io/apuntes/modelado-objetos/"), cargaHoraSemanal = 6)

        repoMaterias.save(algoritmos)
        repoMaterias.save(paradigmas)
        repoMaterias.save(disenio)
        repoMaterias.save(Materia(nombre = "Sistemas Operativos", anio = 2, codigo = "08-2027", sitioWeb = URL("https://www.utnso.com.ar/"), cargaHoraSemanal = 8))

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