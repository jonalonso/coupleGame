package com.jsalazar.couplegame.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.jsalazar.couplegame.R
import com.jsalazar.couplegame.constants.Constants
import com.jsalazar.couplegame.constants.QuestionMode
import com.jsalazar.couplegame.generator.QuestionAdapter

class SettingsActivity : AppCompatActivity() {

    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var spinner: Spinner
    private var adView: AdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val editText = findViewById<EditText>(R.id.editTextQuestion)
        val buttonAdd = findViewById<Button>(R.id.buttonAddQuestion)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewQuestions)
        val adViewContainer = findViewById<FrameLayout>(R.id.ad_view_container)
        spinner = findViewById(R.id.spinnerQuestionMode)

        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_MAIN_KEY, MODE_PRIVATE)
        val selectedMode = sharedPreferences.getString(Constants.SHARED_PREFERENCES_QUESTION_MODE,QuestionMode.ALL.name)

        val questions = loadQuestions().toMutableList()

        questionAdapter = QuestionAdapter(questions) { question ->
            val index = questions.indexOf(question)
            if (index >= 0) {
                questions.removeAt(index)
                if(questions.size == 0 && selectedMode == QuestionMode.CUSTOM_ONLY.name){
                    setQuestionMode(QuestionMode.ALL)
                    spinner.setSelection(0)
                }
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

        ArrayAdapter.createFromResource(
            this,
            R.array.question_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        val position = if (selectedMode == QuestionMode.CUSTOM_ONLY.name) {
            2
        } else if (selectedMode == QuestionMode.DEFAULT_ONLY.name) {
            1
        } else {
            0
        }
        spinner.setSelection(position)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> setQuestionMode(QuestionMode.ALL)
                    1 -> setQuestionMode(QuestionMode.DEFAULT_ONLY)
                    2 -> if (questions.isEmpty()) {
                        Toast.makeText(
                            this@SettingsActivity,
                            R.string.game_mode_error,
                            Toast.LENGTH_SHORT
                        ).show()
                        spinner.setSelection(0)
                        setQuestionMode(QuestionMode.ALL)
                    } else {
                        setQuestionMode(QuestionMode.CUSTOM_ONLY)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        adView = AdView(this)
        adView?.adUnitId = "ca-app-pub-4157328728945876/2749804889"
        adView?.setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, 360))
        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)

        adViewContainer.removeAllViews()
        if(adView!=null){
            adViewContainer.addView(adView)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setQuestionMode(mode: QuestionMode) {
        sharedPreferences.edit().putString(Constants.SHARED_PREFERENCES_QUESTION_MODE, mode.name).apply()

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