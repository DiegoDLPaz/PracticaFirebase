package com.example.practicafirebase

import java.io.Serializable

data class Pokemon(var nombre: String? = null
                   ,var tipo : String? = null,
                   var nivel : Int? = -1,
                   var personalidad: String? = null,
                   var autor: String? = null) : Serializable {
                        var id: String = ""
                   }