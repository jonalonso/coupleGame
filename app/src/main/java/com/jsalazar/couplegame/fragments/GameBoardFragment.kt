package com.jsalazar.couplegame.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import com.jsalazar.couplegame.R
import com.jsalazar.couplegame.databinding.FragmentGameBoardBinding
import kotlinx.coroutines.runBlocking

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GameBoardFragment : Fragment() {

    private var _binding: FragmentGameBoardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGameBoardBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rouletteImage.setOnClickListener {
            spinRoulette()
        }
    }

    private fun spinRoulette() = runBlocking {
        // Generamos un ángulo aleatorio para girar la ruleta
        val randomAngle = (360..3600).random() // Rango de 360° a 3600° para simular varios giros

        // Crear la animación de rotación
        val rotateAnimation = RotateAnimation(
            0f, randomAngle.toFloat(),
            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 1700
            fillAfter = true // Mantener la ruleta en su lugar después de girar
            interpolator = DecelerateInterpolator()
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    val popUpFragment = CardFragment()
                    popUpFragment.show((activity as AppCompatActivity).supportFragmentManager,context?.getString(
                        R.string.app_name
                    ))
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
        binding.rouletteImage.startAnimation(rotateAnimation)
    }







    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}