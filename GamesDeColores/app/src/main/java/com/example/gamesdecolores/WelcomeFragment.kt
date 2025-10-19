package com.example.gamesdecolores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


class WelcomeFragment : Fragment() {

    //

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Simplemente "inflamos" (creamos) la vista y la devolvemos
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Aquí está el cambio principal ---
        // Usamos findViewById para encontrar cada vista que necesitamos
        val startButton: Button = view.findViewById(R.id.startGameButton)
        val rulesButton: Button = view.findViewById(R.id.rulesButton)

        // Navegar al juego al presionar el botón
        startButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_gameFragment)
        }

        // Mostrar el diálogo de reglas
        rulesButton.setOnClickListener {
            showGameRulesDialog()
        }
    }

    private fun showGameRulesDialog() {
        // Requisito: Mostrar reglas en un AlertDialog
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.rules_title))
            .setMessage(getString(R.string.rules_message))
            .setPositiveButton(getString(R.string.rules_ok_button)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }


}