package com.example.gamesdecolores

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ResultFragment : Fragment() {

    // Declaramos las vistas que necesitaremos
    private lateinit var finalScoreTextView: TextView
    private lateinit var highScoreTextView: TextView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var playAgainButton: Button

    // Recibe el argumento del puntaje final


    companion object {
        private val scoreHistory = mutableListOf<Int>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout directamente
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Aquí está el cambio ---
        // Inicializamos todas las vistas
        finalScoreTextView = view.findViewById(R.id.finalScoreTextView)
        highScoreTextView = view.findViewById(R.id.highScoreTextView)
        historyRecyclerView = view.findViewById(R.id.historyRecyclerView)
        playAgainButton = view.findViewById(R.id.playAgainButton)

        // El resto del código funciona igual
        val finalScore = arguments?.getInt("finalScore") ?: 0 // ¡NUEVA LÍNEA!
        finalScoreTextView.text = getString(R.string.final_score_format, finalScore)

        scoreHistory.add(finalScore)

        updateHighScore(finalScore)
        setupRecyclerView()

        playAgainButton.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_gameFragment)
        }
    }

    private fun updateHighScore(currentScore: Int) {
        // Requisito: Usar SharedPreferences
        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE) ?: return
        val highScore = sharedPref.getInt(getString(R.string.high_score_key), 0)

        if (currentScore > highScore) {

            // --- CÓDIGO QUE SÍ FUNCIONA ---
            with(sharedPref.edit()) {
                putInt(getString(R.string.high_score_key), currentScore)
                apply()
            }
            // --- FIN DEL CAMBIO ---

            highScoreTextView.text = getString(R.string.new_high_score_format, currentScore)
        } else {
            highScoreTextView.text = getString(R.string.high_score_format, highScore)
        }
    }
    private fun setupRecyclerView() {
        // Requisito: RecyclerView
        historyRecyclerView.layoutManager = LinearLayoutManager(context)
        historyRecyclerView.adapter = ScoreHistoryAdapter(scoreHistory)
    }
}