package com.uqbar.profesores.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.net.URL

@Entity
class Materia(
   @Column var nombre: String = "",
   @Column var anio: Int = 1,
   @Column var codigo: String = "",
   @Column var sitioWeb: URL?,
   @Column var cargaHoraSemanal: Int = 0
) {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   var id: Long = 0
}