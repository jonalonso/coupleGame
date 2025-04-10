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
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.jsalazar.couplegame.constants.Constants
import com.jsalazar.couplegame.databinding.FragmentGameBoardBinding
import kotlinx.coroutines.runBlocking

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GameBoardFragment : Fragment() {

    private var _binding: FragmentGameBoardBinding? = null

    private val binding get() = _binding!!
    private var adView: AdView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGameBoardBinding.inflate(inflater, container, false)
        val prefs = requireActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_MAIN_KEY, AppCompatActivity.MODE_PRIVATE)
        val isFirstTime = prefs.getBoolean(Constants.SHARED_PREFERENCES_FIRST_TIME_KEY, true)
        if (isFirstTime) {
            IntroDialogFragment().show(parentFragmentManager, "intro")
            prefs.edit().putBoolean(Constants.SHARED_PREFERENCES_FIRST_TIME_KEY, false).apply()
        }

        adView = AdView(this.requireActivity().applicationContext)
        adView?.adUnitId = "ca-app-pub-4157328728945876/2749804889"
        adView?.setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this.requireActivity().applicationContext, 360))
        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)
        binding.adViewContainer.removeAllViews()
        if(adView!=null && !isFirstTime){
            binding.adViewContainer.addView(adView)
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rouletteImage.setOnClickListener {
            spinRoulette()
        }

        parentFragmentManager.setFragmentResultListener(
            "dialog_closed", viewLifecycleOwner
        ) { _, _ ->
            println("hola mundo")
            binding.adViewContainer.visibility = View.VISIBLE
            val adRequest = AdRequest.Builder().build()
            adView?.loadAd(adRequest)

            binding.adViewContainer.removeAllViews()
            if (adView != null) {
                binding.adViewContainer.addView(adView)
            }
        }
    }

    private fun spinRoulette() = runBlocking {
        val randomAngle = (360..3600).random()

        val rotateAnimation = RotateAnimation(
            0f, randomAngle.toFloat(),
            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 1700
            fillAfter = true
            interpolator = DecelerateInterpolator()
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    binding.adViewContainer.visibility = View.GONE
                    binding.adViewContainer.removeAllViews()
                    adView?.destroy()

                    val popUpFragment = CardFragment()
                    popUpFragment.show(parentFragmentManager, "CardFragment")

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