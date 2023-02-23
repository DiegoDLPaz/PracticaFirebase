package com.example.practicafirebase

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var botonIniciarSesion: Button = findViewById(R.id.botonIniciarSesion)
        var botonRegistrar: Button = findViewById(R.id.botonRegistrar)

        var emailText: EditText = findViewById(R.id.emailField)
        var pswText: EditText = findViewById(R.id.pswField)

        var contraOlvidada: TextView = findViewById(R.id.contraOlvidada)

        auth = Firebase.auth

        botonRegistrar.setOnClickListener {
            if (emailText.text.isNotEmpty() && pswText.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    emailText.text.toString(), pswText.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            Toast.makeText(baseContext, "Usuario creado.",
                                Toast.LENGTH_SHORT).show()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", it.exception)
                            Toast.makeText(baseContext, "El usuario ya existe.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        botonIniciarSesion.setOnClickListener {
            if (emailText.text.isNotEmpty() && pswText.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    emailText.text.toString(), pswText.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "findUserWithEmail:success")
                            val miIntent = Intent(this, PantallaPokemon::class.java)
                            val user = auth.currentUser
                            val usuarioEmail = user?.email
                            miIntent.putExtra("eMail", usuarioEmail)
                            startActivity(miIntent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "findUserWithEmail:failure", it.exception)
                            Toast.makeText(baseContext, "Ha habido un error, asegurese de que los datos son correctos.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        contraOlvidada.setOnClickListener {
            val miIntent = Intent(this, RecuperarContrasegna::class.java)
            startActivity(miIntent)
        }
    }

}