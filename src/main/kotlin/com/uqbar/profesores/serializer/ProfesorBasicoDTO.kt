package com.uqbar.profesores.serializer

import com.uqbar.profesores.domain.Profesor

class ProfesorBasicoDTO(profesor: Profesor) {
    var id = profesor.id
    var nombreCompleto = profesor.nombreCompleto
}