package com.example.practicafirebase

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adaptador(var listaPokemon: ArrayList<Pokemon>, var usuario: String) :
    RecyclerView.Adapter<Adaptador.layout_pokemon>() {

    inner class layout_pokemon(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var imagen: ImageView
        var nombre: TextView
        var personalidad: TextView
        var nivel:TextView
        var editar: Button

        init {
            imagen = itemView.findViewById(R.id.TipoImg)
            nombre = itemView.findViewById(R.id.nombrePoke)
            personalidad = itemView.findViewById(R.id.personalidadPoke)
            nivel = itemView.findViewById(R.id.nivelPoke)
            editar = itemView.findViewById(R.id.botonEditar)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): layout_pokemon {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.layout_pokemon, parent, false)
        return layout_pokemon(item)
    }

    override fun onBindViewHolder(holder: layout_pokemon, position: Int) {
        var pokemonActual = listaPokemon[position]
        if(pokemonActual.tipo?.lowercase().equals("agua")){
            holder.imagen.setImageResource(R.drawable.agua)
        }else if(pokemonActual.tipo?.lowercase().equals("planta")){
            holder.imagen.setImageResource(R.drawable.planta)
        }else if(pokemonActual.tipo?.lowercase().equals("fuego")){
            holder.imagen.setImageResource(R.drawable.fuego)
        }
        val pokemonActualAutor = pokemonActual.autor.toString()
        if(pokemonActualAutor != usuario) {
            holder.editar.isEnabled = false
        }
        holder.editar.setOnClickListener {
            val miIntent = Intent(holder.itemView.context, MenuEditar::class.java)
            miIntent.putExtra("POKEMON_EXTRA", pokemonActual)
            miIntent.putExtra("email", pokemonActualAutor)
            holder.itemView.context.startActivity(miIntent)
        }
        holder.nombre.text = pokemonActual.nombre
        holder.nivel.text = pokemonActual.nivel.toString()
        holder.personalidad.text = pokemonActual.personalidad
    }

    override fun getItemCount() = listaPokemon.size

}