package com.uqbar.profesores.domain

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import java.net.URL

@JsonTypeInfo(
   use = JsonTypeInfo.Id.NAME,
   include = JsonTypeInfo.As.PROPERTY,
   property = "__typename"
)
@JsonSubTypes(
   JsonSubTypes.Type(value = MateriaInteresante::class, name = "MateriaInteresante"),
   JsonSubTypes.Type(value = MateriaDesafiante::class, name = "MateriaDesafiante")
)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Materia() {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   var id: Long = 0

   @Column var nombre: String = ""
   @Column var anio: Int = 1
   @Column var codigo: String = ""
   @Column lateinit var sitioWeb: URL
   @Column var cargaHoraSemanal: Double = 0.0

   open fun cargaTotal() = cargaHoraSemanal + cargaHorasExtra()
   abstract fun cargaHorasExtra(): Double
}

@Entity
class MateriaInteresante() : Materia() {
   @Column var gradoDeInteres: Int = 0

   override fun cargaHorasExtra() = cargaHoraSemanal * if (gradoDeInteres > 50) 0.2 else 0.1
}

@Entity
class MateriaDesafiante() : Materia() {
   @Column var cargaHorasExtra: Double = 0.0
   @Column var momentoDificil: Boolean = false

   override fun cargaHorasExtra() = if (momentoDificil) cargaHorasExtra else 10.0
}