package com.nomorelateness.donotlate.feature.minigame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.databinding.FragmentMiniGameBinding
import com.nomorelateness.donotlate.feature.main.presentation.view.MainFragment


class MiniGameFragment : Fragment() {

    private var _binding: FragmentMiniGameBinding? = null
    private val binding: FragmentMiniGameBinding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMiniGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton()

        binding.btnGameBackgroundLadder.setOnClickListener {
            Toast.makeText(requireContext(), "준비중인 기능입니다.", Toast.LENGTH_LONG).show()
        }

        binding.btnGameBackgroundWheel.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .replace(R.id.frame, RouletteFragment())
                .addToBackStack("RouletteFragment")
                .commit()
        }
    }

    private fun backButton() {
        binding.ivGameBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .replace(R.id.frame, MainFragment())
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        parentFragmentManager.popBackStack()
        _binding = null
    }

}