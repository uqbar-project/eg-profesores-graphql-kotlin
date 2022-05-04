package com.uqbar.profesores.domain

import javax.persistence.*

@Entity
class Materia(@Column var nombre: String = "", @Column var anio: Int = 0) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}