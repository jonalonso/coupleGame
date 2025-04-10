package com.jsalazar.couplegame.generator

import android.content.Context
import android.util.Log
import com.jsalazar.couplegame.constants.QuestionMode
import com.jsalazar.couplegame.data.question
import org.json.JSONObject
import java.util.Locale

class questionGenerator {
    companion object {
        fun generate(context: Context, customQuestions: List<String>, mode: String) : ArrayList<question> {
            val challenges = ArrayList<question>()
            try {
                if(mode == QuestionMode.ALL.name || mode == QuestionMode.DEFAULT_ONLY.name) {
                    val inputStream = context.assets.open("challenges.json")
                    val jsonString = inputStream.bufferedReader().use { it.readText() }

                    val jsonObject = JSONObject(jsonString)

                    val jsonArray = jsonObject.getJSONArray("challenges")

                    val locale = Locale.getDefault().language

                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)

                        val challengeText = if (obj.has(locale)) {
                            obj.getString(locale)
                        } else {
                            obj.getString("en") // Fallback
                        }

                        challenges.add(question(challengeText))
                    }
                }
                if(mode == QuestionMode.ALL.name || mode == QuestionMode.CUSTOM_ONLY.name) {
                    customQuestions.forEach { element ->
                        challenges.add(question(element))
                    }
                }

            } catch (e: Exception) {
                Log.e("ChallengeLoader", "Error loading challenges: ${e.message}")
            }


            return challenges
        }
    }
}