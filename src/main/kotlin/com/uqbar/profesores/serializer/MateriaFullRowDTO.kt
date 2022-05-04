package com.uqbar.profesores.serializer

import org.springframework.beans.factory.annotation.Value

interface MateriaFullRowDTO {
    fun getId(): Long
    @Value("#{target.nombre}")
    fun getNombreLindo(): String
    fun getAnio(): Int
    fun getProfesorId(): Long
    fun getProfesorNombre(): String
}

data class MateriaDTO(val id: Long, val nombre: String, val anio: Int, val profesores: List<ProfesorDTO>)

data class ProfesorDTO(val id: Long, val nombre: String)