package com.uqbar.profesores.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
    @ManyToOne lateinit var materia: Materia
    @Column var cantidadInscriptos: Int = 0
    @Column var turno: Turno = Turno.NOCHE
}

enum class Turno {
    MANIANA, TARDE, NOCHE
}