package com.jsalazar.couplegame.generator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jsalazar.couplegame.R

class QuestionAdapter(
    private var questions: List<String>,
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textViewQuestion)
        val buttonDelete: ImageButton = view.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.textView.text = question
        holder.buttonDelete.setOnClickListener {
            onDeleteClick(question)
        }
    }

    override fun getItemCount(): Int = questions.size

    fun updateQuestions(newList: List<String>) {
        questions = newList
        notifyDataSetChanged()
    }
}
