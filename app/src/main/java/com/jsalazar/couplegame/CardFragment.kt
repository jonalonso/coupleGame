package com.jsalazar.couplegame

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.jsalazar.couplegame.databinding.FragmentCardBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CardFragment : DialogFragment() {

    private var _binding: FragmentCardBinding? = null
    private lateinit var FlipAnim:AnimatorSet;
    private lateinit var FlipAnimReverse:AnimatorSet;
    private lateinit var ShowAnim:AnimatorSet;

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCardBinding.inflate(inflater, container, false)

        val scale:Float? = context?.resources?.displayMetrics?.density;

        if(scale is Float){
            binding.FrontCard.cameraDistance = 8000 * scale;
            binding.BackCard.cameraDistance = 8000 * scale;
            FlipAnim = AnimatorInflater.loadAnimator(context,R.animator.flip_animator) as AnimatorSet;
            FlipAnim.setTarget(binding.FrontCard);
            FlipAnimReverse = AnimatorInflater.loadAnimator(context,R.animator.flip_animator_reverse) as AnimatorSet;
            FlipAnimReverse.setTarget(binding.BackCard);
            ShowAnim= AnimatorInflater.loadAnimator(context,R.animator.show_animator) as AnimatorSet;
            ShowAnim.setTarget(binding.buttonSecond);

            Handler(Looper.getMainLooper()).postDelayed(
                {
                    FlipAnim.start();
                    FlipAnimReverse.start();
                },
                500
            )

            Handler(Looper.getMainLooper()).postDelayed(
                {
                    ShowAnim.start();
                },
                1500
            )
        }
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDialog()?.getWindow()?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT))

        binding.buttonSecond.setOnClickListener {
            dismiss();
        //    findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}