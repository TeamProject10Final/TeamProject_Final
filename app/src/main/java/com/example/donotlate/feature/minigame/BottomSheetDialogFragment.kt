package com.example.donotlate.feature.minigame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.donotlate.databinding.FragmentBottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val rouletteViewModel: RouletteViewModel by viewModels()

    private var _binding: FragmentBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetDialogBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGameBottom.setOnClickListener {
            dismiss()
        }

//        binding.btnGameBottom.setOnClickListener {
//
//            val resultList = listOf<String>(
//                binding.etPrize1.text.toString(),
//                binding.etPrize2.text.toString(),
//                binding.etPrize3.text.toString(),
//                binding.etPrize4.text.toString(),
//                binding.etPrize5.text.toString(),
//                binding.etPrize6.text.toString(),
//                binding.etPrize7.text.toString(),
//                binding.etPrize8.text.toString()
//            )
//            rouletteViewModel.updateText(resultList)
//        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}