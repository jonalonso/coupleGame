package com.jsalazar.couplegame.fragments

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.jsalazar.couplegame.R
import com.jsalazar.couplegame.constants.Constants
import com.jsalazar.couplegame.constants.QuestionMode
import com.jsalazar.couplegame.data.question
import com.jsalazar.couplegame.databinding.FragmentCardBinding
import com.jsalazar.couplegame.generator.questionGenerator
import kotlin.random.Random
import android.media.MediaPlayer

class CardFragment : DialogFragment() {

    private var _binding: FragmentCardBinding? = null
    private lateinit var FlipAnim:AnimatorSet
    private lateinit var FlipAnimReverse:AnimatorSet
    private lateinit var ShowAnim:AnimatorSet
    private lateinit var sharedPreferences: SharedPreferences
    private var cardFlipSound: MediaPlayer? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCardBinding.inflate(inflater, container, false)
        sharedPreferences = this.requireActivity().getSharedPreferences(
            Constants.SHARED_PREFERENCES_MAIN_KEY,
            AppCompatActivity.MODE_PRIVATE
        )

        val scale:Float? = context?.resources?.displayMetrics?.density

        val customQuestions = loadCustomQuestions()
        val mode = sharedPreferences.getString(Constants.SHARED_PREFERENCES_QUESTION_MODE, QuestionMode.ALL.name)
        val questions:ArrayList<question> = questionGenerator.generate(this.requireActivity().applicationContext,customQuestions,mode!!)


        if(scale is Float){
            binding.FrontCard.cameraDistance = 8000 * scale
            binding.BackCard.cameraDistance = 8000 * scale
            FlipAnim = AnimatorInflater.loadAnimator(context, R.animator.flip_animator) as AnimatorSet
            FlipAnim.setTarget(binding.FrontCard)
            FlipAnimReverse = AnimatorInflater.loadAnimator(context,
                R.animator.flip_animator_reverse
            ) as AnimatorSet
            FlipAnimReverse.setTarget(binding.BackCard)
            ShowAnim= AnimatorInflater.loadAnimator(context, R.animator.show_animator) as AnimatorSet
            ShowAnim.setTarget(binding.buttonSecond)
            val random:Int = Random.nextInt(questions.size)
            binding.cardLabel.setText(questions.get(random).questionText)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    cardFlipSound = MediaPlayer.create(context, R.raw.flipcard)
                    cardFlipSound?.start()
                    FlipAnim.start()
                    FlipAnimReverse.start()
                },
                500
            )

            Handler(Looper.getMainLooper()).postDelayed(
                {
                    ShowAnim.start()
                },
                1500
            )
        }
        return binding.root
    }

    private fun loadCustomQuestions(): List<String> {
        val set = sharedPreferences.getStringSet(Constants.SHARED_PREFERENCES_QUESTIONS_KEY, emptySet())
        return set?.toList() ?: emptyList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDialog()?.getWindow()?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT))

        binding.buttonSecond.setOnClickListener {
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        parentFragmentManager.setFragmentResult("dialog_closed", Bundle())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        cardFlipSound?.release()
        cardFlipSound = null
        _binding = null
    }
}