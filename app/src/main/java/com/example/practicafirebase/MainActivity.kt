package com.example.practicafirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {

    lateinit var adaptadorPokemon: Adaptador
    lateinit var recyclerPokemon: RecyclerView
    lateinit var listaPokemon: ArrayList<Pokemon>
    lateinit var listaBusqueda: ArrayList<Pokemon>
    lateinit var botonborrar: Button
    lateinit var botonanadir: FloatingActionButton
    lateinit var materialTool: MaterialToolbar
    lateinit var conexion : FirebaseFirestore
    lateinit var botonnivel: FloatingActionButton
    lateinit var funcionBuscar : ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        materialTool = findViewById(R.id.toolbar)

        setSupportActionBar(materialTool)
        materialTool.setBackgroundColor(getColor(R.color.Esavaina))

        var nivelActivado: Boolean = false
        botonnivel = findViewById(R.id.botonNivel)

        recyclerPokemon = findViewById(R.id.RecycleViewPokemon)
        botonanadir=findViewById(R.id.botonAnadir)

        listaPokemon = ArrayList()
        listaBusqueda = ArrayList()

        listaPokemon.add(Pokemon("Vaporeon","Agua",30,"Sexy"))
        listaPokemon.add(Pokemon("Bulbassur","Agua",10,"Weon"))

        conexion = FirebaseFirestore.getInstance()

        MostrarFireBase()

        adaptadorPokemon = Adaptador(listaPokemon)
        recyclerPokemon.adapter = adaptadorPokemon
        recyclerPokemon.layoutManager = GridLayoutManager(this,1)

        conexion.collection("Pokemon").whereEqualTo("nombre","Pepe")

        botonanadir.setOnClickListener{

            var miIntent = Intent(this,insertarPokemon::class.java)
            startActivity(miIntent)

        }

        botonnivel.setOnClickListener{
            if (!nivelActivado){
                MostrarFireBasePorNivel()
                nivelActivado=true
            }else{
                MostrarFireBase()
                nivelActivado=false
            }

        }


    }

    fun MostrarFireBase() {
        val funcionBuscar = conexion.collection("Pokemon").addSnapshotListener{
                value,error ->

            if (error==null){
                if (value!=null){
                    listaPokemon.clear()
                    for (documento in value.documents){

                        println(documento.data)

                        val p : Pokemon? = documento.toObject( Pokemon::class.java)
                        if (p != null) {
                            p.id =  documento.id
                        }
                        if (p != null) {
                            listaPokemon.add(p)
                            adaptadorPokemon.notifyItemChanged(listaPokemon.size - 1)
                            println("Hola soy el pokemon "+p.nombre)
                        }
                    }
                }
            }
        }
    }

    fun MostrarFireBasePorNivel() {
        val funcionBuscar = conexion.collection("Pokemon").orderBy("nivel",Query.Direction.DESCENDING).addSnapshotListener{
                value,error ->
            val coleccion = conexion.collection("Pokemon")
            if (error==null){
                if (value!=null){
                    listaPokemon.clear()

                    for (documento in value.documents){

                        println(documento.data)

                        val p : Pokemon? = documento.toObject( Pokemon::class.java)
                        if (p != null) {
                            p.id =  documento.id
                        }
                        if (p != null) {
                            listaPokemon.add(p)
                            adaptadorPokemon.notifyItemChanged(listaPokemon.size - 1)
                            println("Hola soy el pokemon "+p.nombre)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView?
        val basura = menu.findItem(R.id.borrar)
        searchView!!.maxWidth = Int.MAX_VALUE

        searchView.setOnCloseListener {
            listaPokemon.clear()
            MostrarFireBase()
            false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(texto: String?): Boolean {
                listaBusqueda.clear()
                for (pokemon in listaPokemon){
                    if (texto != null) {
                        println(pokemon.nombre?.take(texto.length))
                        if(pokemon.nombre?.take(texto.length).equals(texto)){
                            listaBusqueda.add(pokemon)
                        }else{
                        }
                    }
                }


                adaptadorPokemon = Adaptador(listaBusqueda)
                recyclerPokemon.adapter = adaptadorPokemon

                return false
            }
        })
        return true
    }
}
