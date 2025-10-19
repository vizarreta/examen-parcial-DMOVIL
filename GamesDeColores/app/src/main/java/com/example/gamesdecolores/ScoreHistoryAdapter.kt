package com.example.gamesdecolores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScoreHistoryAdapter(private val scores: List<Int>) :
    RecyclerView.Adapter<ScoreHistoryAdapter.ScoreViewHolder>() {

    // Define cómo se ve cada "fila" de la lista
    // Este código ya usa findViewById por defecto
    class ScoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val scoreText: TextView = view.findViewById(R.id.item_score_text)
    }

    // Crea una nueva "fila" (ViewHolder)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_score, parent, false)
        return ScoreViewHolder(view)
    }

    // Conecta los datos (el puntaje) con la "fila"
    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val score = scores[position]
        holder.scoreText.text =
            holder.itemView.context.getString(R.string.history_item_format, position + 1, score)
    }

    // Le dice al RecyclerView cuántos ítems hay en la lista
    override fun getItemCount() = scores.size
}