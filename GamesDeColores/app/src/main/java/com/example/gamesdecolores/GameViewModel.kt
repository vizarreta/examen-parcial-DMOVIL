package com.example.gamesdecolores

import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    // Aquí guardamos los datos que deben sobrevivir a la rotación
    var score = 0
    var remainingTime = 30000L // 30 segundos en milisegundos
    var isTimerRunning = false
}