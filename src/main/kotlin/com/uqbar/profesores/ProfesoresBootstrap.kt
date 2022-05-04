package com.uqbar.profesores

import com.uqbar.profesores.domain.Materia
import com.uqbar.profesores.domain.Profesor
import com.uqbar.profesores.repos.MateriaRepository
import com.uqbar.profesores.repos.ProfesorRepository
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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
        val	algoritmos = Materia("Algoritmos y Estructura de Datos", 1)
        val paradigmas = Materia("Paradigmas de Programacion", 2)
        val disenio = Materia("Diseño de Sistemas", 3)

        repoMaterias.save(algoritmos)
        repoMaterias.save(paradigmas)
        repoMaterias.save(disenio)
        repoMaterias.save(Materia("Sistemas Operativos", 2))

        val spigariol = Profesor("Lucas Spigariol")
        spigariol.agregarMateria(algoritmos)
        spigariol.agregarMateria(paradigmas)

        val passerini = Profesor("Nicolás Passerini")
        passerini.agregarMateria(paradigmas)
        passerini.agregarMateria(disenio)

        val dodino = Profesor("Fernando Dodino")
        dodino.agregarMateria(disenio)

        repoProfes.save(spigariol)
        repoProfes.save(passerini)
        repoProfes.save(dodino)

    }
}