package com.uqbar.profesores.domain

import java.net.URL
import javax.persistence.*

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