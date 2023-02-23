package com.example.practicafirebase

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecuperarContrasegna : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_contrasegna)

        auth = Firebase.auth
        var botonVolver: Button = findViewById(R.id.botonVolver)
        var botonRecuperar: Button = findViewById(R.id.botonRecuperar)
        var emailText: EditText = findViewById(R.id.emailText)

        botonRecuperar.setOnClickListener {
            if (emailText.text.toString().isEmpty()) {
                return@setOnClickListener
            }else if (Patterns.EMAIL_ADDRESS.matcher(emailText.text.toString()).matches()) {
                auth.sendPasswordResetEmail(emailText.text.toString())
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "sendEmail:success")
                        Toast.makeText(baseContext, "E-Mail enviado.",
                            Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { error ->
                        val errorMessage = error.message
                        Log.d(ContentValues.TAG, "sendEmail:failure")
                        Toast.makeText(baseContext, "Error! $errorMessage",
                            Toast.LENGTH_SHORT).show()
                    }
            }
        }

        botonVolver.setOnClickListener {
            var miIntent = Intent(this, LoginActivity::class.java)
            startActivity(miIntent)
        }

    }
}