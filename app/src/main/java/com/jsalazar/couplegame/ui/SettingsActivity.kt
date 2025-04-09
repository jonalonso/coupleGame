package com.jsalazar.couplegame.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jsalazar.couplegame.R
import com.jsalazar.couplegame.constants.Constants
import com.jsalazar.couplegame.generator.QuestionAdapter

class SettingsActivity : AppCompatActivity() {

    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val editText = findViewById<EditText>(R.id.editTextQuestion)
        val buttonAdd = findViewById<Button>(R.id.buttonAddQuestion)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewQuestions)

        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_MAIN_KEY, MODE_PRIVATE)

        val questions = loadQuestions().toMutableList()

        questionAdapter = QuestionAdapter(questions) { question ->
            val index = questions.indexOf(question)
            if (index >= 0) {
                questions.removeAt(index)
                saveQuestions(questions)
                questionAdapter.updateQuestions(questions)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = questionAdapter

        buttonAdd.setOnClickListener {
            val question = editText.text.toString().trim()
            if (question.isNotEmpty()) {
                questions.add(question)
                questionAdapter.notifyItemInserted(questions.size - 1)
                questionAdapter.updateQuestions(questions)
                saveQuestions(questions)
                editText.text.clear()
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun loadQuestions(): List<String> {
        val set = sharedPreferences.getStringSet(Constants.SHARED_PREFERENCES_QUESTIONS_KEY, emptySet())
        return set?.toList() ?: emptyList()
    }

    private fun saveQuestions(questions: List<String>) {
        sharedPreferences.edit()
            .putStringSet(Constants.SHARED_PREFERENCES_QUESTIONS_KEY, questions.toSet())
            .apply()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}