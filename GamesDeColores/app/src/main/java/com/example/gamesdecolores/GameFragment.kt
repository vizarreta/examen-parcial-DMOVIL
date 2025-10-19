package com.example.gamesdecolores

import android.media.AudioAttributes // ¡Importante!
import android.media.SoundPool // ¡Importante!
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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

class GameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModels()

    // Vistas
    private lateinit var scoreTextView: TextView
    private lateinit var timerTextView: TextView
    private lateinit var colorDisplayView: View
    private lateinit var redButton: Button
    private lateinit var greenButton: Button
    private lateinit var blueButton: Button
    private lateinit var yellowButton: Button

    private lateinit var timer: CountDownTimer
    private var currentColorResId: Int = 0

    // --- ¡CAMBIO PARA SONIDOS! ---
    private lateinit var soundPool: SoundPool
    private var correctSoundId: Int = 0
    private var incorrectSoundId: Int = 0
    // --- FIN CAMBIO ---

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

        // Inicializar vistas (findViewById)
        scoreTextView = view.findViewById(R.id.scoreTextView)
        timerTextView = view.findViewById(R.id.timerTextView)
        colorDisplayView = view.findViewById(R.id.colorDisplayView)
        redButton = view.findViewById(R.id.redButton)
        greenButton = view.findViewById(R.id.greenButton)
        blueButton = view.findViewById(R.id.blueButton)
        yellowButton = view.findViewById(R.id.yellowButton)

        // --- ¡CAMBIO PARA SONIDOS! ---
        setupSoundPool()
        // --- FIN CAMBIO ---

        setupGame()

        if (!viewModel.isTimerRunning) {
            startTimer(viewModel.remainingTime)
        } else {
            startTimer(viewModel.remainingTime)
        }
    }

    // --- ¡NUEVA FUNCIÓN! ---
    private fun setupSoundPool() {
        // Configura el SoundPool para juegos
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(2) // Podemos sonar 2 efectos a la vez
            .setAudioAttributes(audioAttributes)
            .build()

        // Carga los sonidos en memoria para usarlos rápido
        correctSoundId = soundPool.load(requireContext(), R.raw.correct, 1)
        incorrectSoundId = soundPool.load(requireContext(), R.raw.incorrect, 1)
    }
    // --- FIN FUNCIÓN ---

    private fun setupGame() {
        updateScoreDisplay()
        nextColor()
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
            // --- ¡CAMBIO PARA SONIDOS! ---
            soundPool.play(correctSoundId, 1f, 1f, 1, 0, 1f)
            // --- FIN CAMBIO ---
            viewModel.score++
            updateScoreDisplay()
        } else {
            // --- ¡CAMBIO PARA SONIDOS! ---
            // Toca el sonido de error
            soundPool.play(incorrectSoundId, 1f, 1f, 1, 0, 1f)
            // --- FIN CAMBIO ---
        }
        nextColor() // Mover al siguiente color en ambos casos
    }

    private fun updateScoreDisplay() {
        scoreTextView.text = getString(R.string.score_format, viewModel.score)
    }

    private fun startTimer(startTimeInMillis: Long) {
        viewModel.isTimerRunning = true
        timer = object : CountDownTimer(startTimeInMillis, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                viewModel.remainingTime = millisUntilFinished
                timerTextView.text = getString(R.string.time_format, millisUntilFinished / 1000)
            }

            override fun onFinish() {
                viewModel.isTimerRunning = false
                viewModel.remainingTime = 30000L
                val bundle = Bundle()
                bundle.putInt("finalScore", viewModel.score)
                viewModel.score = 0
                findNavController().navigate(R.id.action_gameFragment_to_resultFragment, bundle)
            }
        }.start()
    }

    private fun applyFadeInAnimation(view: View) {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 400
        view.startAnimation(fadeIn)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        // --- ¡CAMBIO PARA SONIDOS! ---
        soundPool.release() // Libera los recursos del soundpool
        // --- FIN CAMBIO ---
    }
}