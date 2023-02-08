package com.example.practicafirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore

class insertarPokemon : AppCompatActivity() {

    lateinit var conexion : FirebaseFirestore
    lateinit var nombre: String
    lateinit var perso: String
    lateinit var tipo: String
    lateinit var insertar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar_pokemon)

        var spinner : Spinner = findViewById(R.id.spinner)

        var infoNombre : TextView = findViewById(R.id.textoNombre)
        var infoPerso : TextView = findViewById(R.id.textoPersonalidad)
        var infoNivel : TextView = findViewById(R.id.textoNivel)

        insertar=findViewById(R.id.insertar)

        var nivel: Int=0

        var listaTipos = ArrayList<String?>()

        conexion = FirebaseFirestore.getInstance()

        listaTipos.add("Agua")
        listaTipos.add("Planta")
        listaTipos.add("Fuego")

        var adaptador = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_item,listaTipos)

        spinner.adapter = adaptador

        spinner.onItemSelectedListener = object :  AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?,
                                        vista: View?,
                                        posi: Int,
                                        id: Long) {
                var seleccionado = spinner.getItemAtPosition(posi)
                tipo=seleccionado.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        insertar.setOnClickListener{

            nombre = infoNombre.text.toString()
            nivel = infoNivel.text.toString().toInt()
            perso = infoNivel.text.toString()

            if (nombre==null || nivel == null || perso == null || tipo == null){
                Toast.makeText(this,"Por favor no se deje datos por introducir",Toast.LENGTH_SHORT).show()
            }else{
                val p = Pokemon(nombre, tipo, nivel , perso)
                conexion.collection("Pokemon").add(p)
                finish()
            }
        }

    }
}