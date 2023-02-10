package com.example.practicafirebase

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.widget.Button
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {

    lateinit var adaptadorPokemon: Adaptador
    lateinit var recyclerPokemon: RecyclerView
    lateinit var listaPokemon: ArrayList<Pokemon>
    lateinit var listaBusqueda: ArrayList<Pokemon>
    lateinit var listaArriba: ArrayList<Pokemon>
    lateinit var botonborrar: Button
    lateinit var botonanadir: FloatingActionButton
    lateinit var materialTool: MaterialToolbar
    lateinit var conexion : FirebaseFirestore
    lateinit var botonnivel: FloatingActionButton
    var arribaActivado: Boolean = false

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
        listaArriba = ArrayList()

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
                    var antes = listaPokemon.size
                    listaPokemon.clear()
                    for (documento in value.documents){

                        val p : Pokemon? = documento.toObject( Pokemon::class.java)
                        if (p != null) {
                            p.id =  documento.id
                        }
                        if (p != null) {
                            listaPokemon.add(p)

                        }
                    }
                    //tell the recycler view that all the old items are gone
                    adaptadorPokemon.notifyItemRangeRemoved(0, antes);
                    //tell the recycler view how many new items we added
                    adaptadorPokemon.notifyItemRangeInserted(0, listaPokemon.size);
                }
            }
        }
    }

    fun MostrarFireBasePorNivel() {
        val funcionBuscar = conexion.collection("Pokemon")
            .orderBy("nivel",Query.Direction.DESCENDING).get().addOnSuccessListener{ value ->
            val coleccion = conexion.collection("Pokemon")
                if (value!=null){
                    var antes = listaPokemon.size
                    listaPokemon.clear()

                    for (documento in value.documents){

                        val p : Pokemon? = documento.toObject( Pokemon::class.java)
                        if (p != null) {
                            p.id =  documento.id
                        }
                        if (p != null) {
                            listaPokemon.add(p)
                        }
                    }
                    //tell the recycler view that all the old items are gone
                    adaptadorPokemon.notifyItemRangeRemoved(0, antes);
                    //tell the recycler view how many new items we added
                    adaptadorPokemon.notifyItemRangeInserted(0, listaPokemon.size);
            }
        }
    }

    @SuppressLint("ServiceCast")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu,menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView?
        val basura = menu.findItem(R.id.borrar)
        val niv = menu.findItem(R.id.arriba)
        val sms = menu.findItem(R.id.sms)

        sms.setOnMenuItemClickListener {

            val smsManager = SmsManager.getDefault()

            var layoutSms = LayoutInflater.from(this)
                .inflate(R.layout.layout_sms, null, false)
            val dialogo = MaterialAlertDialogBuilder(MainActivity@ this)

            dialogo.setTitle("Introduzca el nombre del pokemon a borrar")
            dialogo.setView(layoutSms)
            dialogo.setPositiveButton("OK") { dialog, which ->

                val telefono = layoutSms.findViewById<AppCompatEditText>(R.id.telefono)
                val mensaje = layoutSms.findViewById<AppCompatEditText>(R.id.mensaje)

                smsManager.sendTextMessage(telefono.text.toString(),null,mensaje.text.toString(),null,null)

                Toast.makeText(applicationContext,"Mensaje Enviado",Toast.LENGTH_SHORT).show()

            }
            true
        }


        basura.setOnMenuItemClickListener {

            var layoutDialogo = LayoutInflater.from(this)
                .inflate(R.layout.layout_dialogo, null, false)
            val dialogo = MaterialAlertDialogBuilder(MainActivity@this)

            dialogo.setTitle("Introduzca el nombre del pokemon a borrar")
            dialogo.setView(layoutDialogo)
            dialogo.setPositiveButton("OK") { dialog, which ->

                val nombre = layoutDialogo.findViewById<AppCompatEditText>(R.id.dialogoNombre)
                // buscar pokemon con nombre = pepe.
                // y de este pokemon sacar el id
                val buscado = conexion.collection("Pokemon").
                whereEqualTo("nombre",nombre.text.toString())
                buscado.get().addOnSuccessListener { documents ->
                    for (document in documents){

                        val documentId = document.id
                        val borrar = conexion.collection("Pokemon").document(documentId)

                        borrar.delete().
                        addOnSuccessListener {
                            Log.d("TAG","Se borró con éxito")
                        }.
                        addOnFailureListener{e->
                            Log.d("TAG","No se pudo borrar",e)
                        }
                        //MostrarFireBase()
                    }
                }
            }
            dialogo.setNegativeButton("Cancel", null)
            dialogo.show()
            true
        }

        niv.setOnMenuItemClickListener {
            if (arribaActivado){
                var antes = adaptadorPokemon.listaPokemon .size
                adaptadorPokemon.listaPokemon = listaPokemon
                //tell the recycler view that all the old items are gone
                adaptadorPokemon.notifyItemRangeRemoved(0, antes);
                //tell the recycler view how many new items we added
                adaptadorPokemon.notifyItemRangeInserted(0, listaPokemon.size)
            }else{
                var layoutDialogo = LayoutInflater.from(this)
                    .inflate(R.layout.layout_dialogo, null, false)
                val dialogo = MaterialAlertDialogBuilder(MainActivity@this)

                dialogo.setTitle("Introduzca el nivel del filtro")
                dialogo.setView(layoutDialogo)
                dialogo.setPositiveButton("Filtrar") { dialog, which ->

                    val numero = layoutDialogo.findViewById<AppCompatEditText>(R.id.dialogoNombre)

                    val buscado = conexion.collection("Pokemon").
                    whereGreaterThan("nivel",Integer.parseInt(numero.text.toString()))
                    buscado.get().addOnSuccessListener { value ->

                        if (value!=null){
                            var antes = listaArriba.size
                            listaArriba.clear()
                            for (documento in value.documents){
                                val p : Pokemon? = documento.toObject( Pokemon::class.java)
                                if (p != null) {
                                    listaArriba.add(p)
                                }
                            }
                            //tell the recycler view that all the old items are gone
                            adaptadorPokemon.notifyItemRangeRemoved(0, antes);
                            //tell the recycler view how many new items we added
                            adaptadorPokemon.notifyItemRangeInserted(0, listaArriba.size)
                            adaptadorPokemon = Adaptador(listaArriba)
                            recyclerPokemon.adapter = adaptadorPokemon
                        }
                    }
                }
                arribaActivado
                dialogo.setNegativeButton("Cancel", null)
                dialogo.show()
            }
            arribaActivado = !arribaActivado
            true
        }

        searchView!!.maxWidth = Int.MAX_VALUE

        searchView.setOnCloseListener {
            var antes = adaptadorPokemon.listaPokemon .size
            adaptadorPokemon.listaPokemon = listaPokemon
            //tell the recycler view that all the old items are gone
            adaptadorPokemon.notifyItemRangeRemoved(0, antes);
            //tell the recycler view how many new items we added
            adaptadorPokemon.notifyItemRangeInserted(0, listaPokemon.size);

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
