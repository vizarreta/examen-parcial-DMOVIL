package com.example.gamesdecolores

import android.media.MediaPlayer // ¡Importante!
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    // --- ¡CAMBIO PARA MÚSICA! ---
    private var backgroundMusic: MediaPlayer? = null
    // --- FIN CAMBIO ---

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- ¡CAMBIO PARA MÚSICA! ---
        // Inicializa y arranca la música de fondo
        backgroundMusic = MediaPlayer.create(this, R.raw.background_music)
        backgroundMusic?.isLooping = true // Para que se repita
        backgroundMusic?.setVolume(0.3f, 0.3f) // Bajar volumen para no ser molesto
        backgroundMusic?.start()
        // --- FIN CAMBIO ---
    }

    override fun onPause() {
        super.onPause()
        // --- ¡CAMBIO PARA MÚSICA! ---
        // Pausa la música si la app se va a segundo plano
        backgroundMusic?.pause()
        // --- FIN CAMBIO ---
    }

    override fun onResume() {
        super.onResume()
        // --- ¡CAMBIO PARA MÚSICA! ---
        // Reanuda la música cuando la app vuelve
        if (backgroundMusic?.isPlaying == false) {
            backgroundMusic?.start()
        }
        // --- FIN CAMBIO ---
    }

    override fun onDestroy() {
        super.onDestroy()
        // --- ¡CAMBIO PARA MÚSICA! ---
        // Libera recursos cuando la app se cierra del todo
        backgroundMusic?.stop()
        backgroundMusic?.release()
        backgroundMusic = null
        // --- FIN CAMBIO ---
    }
}