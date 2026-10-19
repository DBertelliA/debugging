package com.example.createthesos

class ClaseA {
    private lateinit var nombre : String

    private lateinit var apellido : String

    private var edad : Int = 0



    constructor()

    constructor(nombre: String, apellido: String, edad: Int) {
        this.nombre = nombre
        this.apellido = apellido
        this.edad = edad
    }

    override fun toString(): String {
        return "ClaseA(nombre='$nombre', apellido='$apellido', edad=$edad)"
    }


}