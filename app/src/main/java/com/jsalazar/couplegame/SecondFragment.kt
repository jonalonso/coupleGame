package com.jsalazar.couplegame

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jsalazar.couplegame.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private lateinit var FlipAnim:AnimatorSet;
    private lateinit var FlipAnimReverse:AnimatorSet;

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        val scale:Float? = context?.resources?.displayMetrics?.density;

        if(scale is Float){
            binding.FrontCard.cameraDistance = 8000 * scale;
            binding.BackCard.cameraDistance = 8000 * scale;
            FlipAnim = AnimatorInflater.loadAnimator(context,R.animator.flip_animator) as AnimatorSet;
            FlipAnim.setTarget(binding.FrontCard);
            FlipAnimReverse = AnimatorInflater.loadAnimator(context,R.animator.flip_animator_reverse) as AnimatorSet;
            FlipAnimReverse.setTarget(binding.BackCard);

            Handler(Looper.getMainLooper()).postDelayed(
                {
                    FlipAnim.start();
                    FlipAnimReverse.start();
                },
                1000 // value in milliseconds
            )
        }
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}