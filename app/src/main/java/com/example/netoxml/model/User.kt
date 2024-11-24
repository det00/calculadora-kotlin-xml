package com.example.netoxml.model

import java.io.Serializable

class User(
    var salario: String,
    var edad: String,
    var pagas: String,
    var discapacidad: String,
    var grupo: String,
    var estadoCivil: String,
    var numHijos: String
    ): Serializable {
}