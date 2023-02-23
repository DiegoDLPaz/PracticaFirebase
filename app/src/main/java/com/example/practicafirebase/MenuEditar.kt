package com.example.practicafirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore

class MenuEditar : AppCompatActivity() {

    lateinit var conexion : FirebaseFirestore
    lateinit var nombre: String
    lateinit var perso: String
    lateinit var tipo: String
    lateinit var actualizar: Button
    lateinit var pokemonActual: Pokemon
    var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_editar)

        val intentT : Intent = intent
        pokemonActual = intentT.getSerializableExtra("POKEMON_EXTRA") as Pokemon
        email = intentT.getStringExtra("email")

        val spinner : Spinner = findViewById(R.id.spinner)
        val infoNombre : TextView = findViewById(R.id.textoNombre)
        val infoPerso : TextView = findViewById(R.id.textoPersonalidad)
        val infoNivel : TextView = findViewById(R.id.textoNivel)
        actualizar = findViewById(R.id.actualizar)
        conexion = FirebaseFirestore.getInstance()

        infoNombre.text = pokemonActual.nombre
        infoPerso.text = pokemonActual.personalidad
        infoNivel.text = pokemonActual.nivel.toString()

        var nivel: Int

        val listaTipos = ArrayList<String?>()
        listaTipos.add("Agua")
        listaTipos.add("Planta")
        listaTipos.add("Fuego")
        val adaptador = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_item,listaTipos)
        spinner.adapter = adaptador
        if(pokemonActual.tipo.equals("Agua")) {
            spinner.setSelection(0)
        }else if(pokemonActual.tipo.equals("Planta")) {
            spinner.setSelection(1)
        }else if(pokemonActual.tipo.equals("Fuego")) {
            spinner.setSelection(2)
        }

        spinner.onItemSelectedListener = object :  AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                vista: View?,
                posi: Int,
                id: Long
            ) {
                val seleccionado = spinner.getItemAtPosition(posi)
                tipo = seleccionado.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        actualizar.setOnClickListener{

            nombre = infoNombre.text.toString()
            perso = infoPerso.text.toString()

            if (nombre.isEmpty() || infoNivel.text.isEmpty() || perso.isEmpty() || tipo.isEmpty()){
                Toast.makeText(this,"Por favor no se deje datos por introducir",Toast.LENGTH_SHORT).show()
            }else{
                nivel = Integer.parseInt(infoNivel.text.toString())
                val pokemonRef = conexion.collection("Pokemon").document(pokemonActual.id)
                pokemonRef.update(
                    "nombre", nombre,
                    "tipo", tipo,
                    "nivel", nivel,
                    "personalidad", perso,
                    "autor", email
                )
                    .addOnSuccessListener {
                        Toast.makeText(this,"Datos actualizados",Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this,"ERROR al actualizar los datos",Toast.LENGTH_SHORT).show()
                    }

            }
        }
    }
}