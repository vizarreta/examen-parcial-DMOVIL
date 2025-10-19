package com.example.gamesdecolores // Asegúrate que el nombre del paquete sea el tuyo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Esto carga el layout que acabas de modificar
    }
}