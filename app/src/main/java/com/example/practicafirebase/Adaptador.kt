package com.example.practicafirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adaptador(var listaPokemon: ArrayList<Pokemon>) :
    RecyclerView.Adapter<Adaptador.layout_pokemon>() {

    inner class layout_pokemon(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var imagen: ImageView
        var nombre: TextView
        var personalidad: TextView
        var nivel:TextView

        init {
            imagen = itemView.findViewById(R.id.TipoImg)
            nombre = itemView.findViewById(R.id.nombrePoke)
            personalidad = itemView.findViewById(R.id.personalidadPoke)
            nivel = itemView.findViewById(R.id.nivelPoke)
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
        val PokemonActual = listaPokemon[position]
        if(PokemonActual.tipo?.lowercase().equals("agua")){
            holder.imagen.setImageResource(R.drawable.agua)
        }else if(PokemonActual.tipo?.lowercase().equals("planta")){
            holder.imagen.setImageResource(R.drawable.planta)
        }else if(PokemonActual.tipo?.lowercase().equals("fuego")){
            holder.imagen.setImageResource(R.drawable.fuego)
        }
        holder.nombre.text = PokemonActual.nombre
        holder.nivel.text = PokemonActual.nivel.toString()
        holder.personalidad.text = PokemonActual.personalidad
    }

    override fun getItemCount() = listaPokemon.size

}