package com.example.gamesdecolores

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels // ¡Importante!
import androidx.navigation.fragment.findNavController

class GameFragment : Fragment() {

    // --- ¡CAMBIO PARA EL 20/20! ---
    // Obtenemos el ViewModel. Esta "caja fuerte" sobrevive a la rotación
    private val viewModel: GameViewModel by viewModels()

    // Declaramos las vistas
    private lateinit var scoreTextView: TextView
    private lateinit var timerTextView: TextView
    private lateinit var colorDisplayView: View
    private lateinit var redButton: Button
    private lateinit var greenButton: Button
    private lateinit var blueButton: Button
    private lateinit var yellowButton: Button

    private lateinit var timer: CountDownTimer
    private var currentColorResId: Int = 0

    // Mapa de colores
    private val gameColors by lazy {
        mapOf(
            R.id.redButton to R.color.game_red,
            R.id.greenButton to R.color.game_green,
            R.id.blueButton to R.color.game_blue,
            R.id.yellowButton to R.color.game_yellow
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializamos las vistas
        scoreTextView = view.findViewById(R.id.scoreTextView)
        timerTextView = view.findViewById(R.id.timerTextView)
        colorDisplayView = view.findViewById(R.id.colorDisplayView)
        redButton = view.findViewById(R.id.redButton)
        greenButton = view.findViewById(R.id.greenButton)
        blueButton = view.findViewById(R.id.blueButton)
        yellowButton = view.findViewById(R.id.yellowButton)

        setupGame()

        // --- ¡CAMBIO PARA EL 20/20! ---
        // Solo inicia el temporizador si no estaba ya corriendo
        if (!viewModel.isTimerRunning) {
            startTimer(viewModel.remainingTime)
        } else {
            // Si ya corría, solo lo recrea con el tiempo que quedaba
            startTimer(viewModel.remainingTime)
        }
    }

    private fun setupGame() {
        // ¡CAMBIO! Leemos el puntaje desde el ViewModel
        updateScoreDisplay()
        nextColor()

        // Asignamos los listeners
        redButton.setOnClickListener { checkAnswer(R.color.game_red) }
        greenButton.setOnClickListener { checkAnswer(R.color.game_green) }
        blueButton.setOnClickListener { checkAnswer(R.color.game_blue) }
        yellowButton.setOnClickListener { checkAnswer(R.color.game_yellow) }
    }

    private fun nextColor() {
        currentColorResId = gameColors.values.random()
        colorDisplayView.setBackgroundColor(ContextCompat.getColor(requireContext(), currentColorResId))
        applyFadeInAnimation(colorDisplayView)
    }

    private fun checkAnswer(selectedColorResId: Int) {
        if (selectedColorResId == currentColorResId) {
            // ¡CAMBIO! Guardamos el puntaje en el ViewModel
            viewModel.score++
            updateScoreDisplay()
        }
        nextColor()
    }

    private fun updateScoreDisplay() {
        // ¡CAMBIO! Leemos el puntaje desde el ViewModel
        scoreTextView.text = getString(R.string.score_format, viewModel.score)
    }

    // ¡CAMBIO! Acepta el tiempo restante como parámetro
    private fun startTimer(startTimeInMillis: Long) {
        viewModel.isTimerRunning = true
        timer = object : CountDownTimer(startTimeInMillis, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                // ¡CAMBIO! Guardamos el tiempo restante en el ViewModel
                viewModel.remainingTime = millisUntilFinished
                timerTextView.text = getString(R.string.time_format, millisUntilFinished / 1000)
            }

            override fun onFinish() {
                viewModel.isTimerRunning = false
                viewModel.remainingTime = 30000L // Reinicia el tiempo para la próxima partida

                // Navega a resultados
                val bundle = Bundle()
                // ¡CAMBIO! Pasamos el puntaje desde el ViewModel
                bundle.putInt("finalScore", viewModel.score)
                viewModel.score = 0 // Reinicia el puntaje para la próxima partida

                findNavController().navigate(R.id.action_gameFragment_to_resultFragment, bundle)
            }
        }.start()
    }

    // Funcionalidad Adicional ObligatorIA: Animación
    private fun applyFadeInAnimation(view: View) {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 400
        view.startAnimation(fadeIn)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel() // Es crucial para evitar fugas de memoria
    }
}