package com.example.practicafirebase

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore

class InsertarPokemon : AppCompatActivity() {

    lateinit var conexion : FirebaseFirestore
    lateinit var nombre: String
    lateinit var perso: String
    lateinit var tipo: String
    lateinit var insertar : Button
    var usuario: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar_pokemon)

        val intentT : Intent = intent
        usuario = intentT.getStringExtra("usuario").toString()

        var spinner : Spinner = findViewById(R.id.spinner)

        var infoNombre : TextView = findViewById(R.id.textoNombre)
        var infoPerso : TextView = findViewById(R.id.textoPersonalidad)
        var infoNivel : TextView = findViewById(R.id.textoNivel)

        insertar=findViewById(R.id.actualizar)

        var nivel: Int

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
            perso = infoPerso.text.toString()

            if (nombre.isEmpty() || infoNivel.text.isEmpty() || perso.isEmpty() || tipo.isEmpty()){
                Toast.makeText(this,"Por favor no se deje datos por introducir",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            nivel = Integer.parseInt(infoNivel.text.toString())
            val p = Pokemon(nombre, tipo, nivel , perso, usuario)

            conexion.collection("Pokemon").add(p)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    Toast.makeText(this, "Error al guardar el Pokemon", Toast.LENGTH_SHORT).show()
                }
        }

    }
}