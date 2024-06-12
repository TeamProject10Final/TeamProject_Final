package com.example.donotlate.feature.minigame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.databinding.FragmentMiniGameBinding
import com.example.donotlate.feature.main.presentation.MainPageViewModel
import com.example.donotlate.feature.main.presentation.MainPageViewModelFactory


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

        binding.ivGameBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.btnGameBackgroundLadder.setOnClickListener {
            Toast.makeText(requireContext(), "준비중인 기능입니다.", Toast.LENGTH_LONG).show()
        }
        binding.btnGameBackgroundWheel.setOnClickListener {
            Toast.makeText(requireContext(), "준비중인 기능입니다.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}