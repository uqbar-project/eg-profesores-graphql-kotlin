package com.uqbar.profesores.domain

import javax.persistence.*

@Entity
class Profesor(
   @Column var nombre: String = "",
   @Column var apellido: String = "",
   @Column var anioComienzo: Int = 0,
   @Column var puntajeDocente: Int = 0
) {
   @Id
   // El GenerationType asociado a la TABLE es importante para tener
   // una secuencia de identificadores única para los profesores
   // (para que no dependa de otras entidades anteriormente creadas)
   @GeneratedValue(strategy = GenerationType.TABLE)
   var id: Long = 0

   @ManyToMany(fetch = FetchType.LAZY)
   var materias: MutableSet<Materia> = HashSet()

   fun agregarMateria(materia: Materia) {
      materias.add(materia)
   }
}