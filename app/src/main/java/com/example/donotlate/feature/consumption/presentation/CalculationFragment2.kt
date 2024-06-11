package com.example.donotlate.feature.consumption.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentCalculation2Binding

class CalculationFragment2 : Fragment(R.layout.fragment_calculation2) {

    private var _binding: FragmentCalculation2Binding ?= null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        SharedViewModelFactory(
            appContainer.insertConsumptionUseCase,
            appContainer.deleteConsumptionUseCase,
            appContainer.getDataCountUseCase
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculation2Binding.inflate(layoutInflater, container, false)
        return binding.root
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 이전에 입력한 내용이 있다면 해당 내용을 EditText에 설정
        viewModel.total.value?.let { binding.etDes21.setText(it) }
        viewModel.isPenalty.value?.let { if(it) binding.btnPenalty.setText("나의 벌금추가") else binding.btnPenalty.setText("나의 벌금추가") }
        viewModel.penalty.value?.let { binding.etDes22.setText(it) }
        viewModel.number.value?.let { binding.etDes23.setText(it) }

        binding.btnPenalty.setOnClickListener {
            viewModel.changeIsPenalty(viewModel.isPenalty.value!!)
            ConsumptionActivity.hideKeyboard(view)
        }

        viewModel.isPenalty.observe(viewLifecycleOwner){isPenalty ->
            updateIsPenaltyButton(isPenalty)
        }

        binding.etDes21.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding.etDes21.clearFocus()
                ConsumptionActivity.hideKeyboard(view)
            }
        }

        binding.etDes22.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding.etDes22.clearFocus()
                ConsumptionActivity.hideKeyboard(view)
            }
        }

        binding.etDes23.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding.etDes23.clearFocus()
                ConsumptionActivity.hideKeyboard(view)
            }
        }

        binding.btnCalEnd.setOnClickListener {
            ConsumptionActivity.hideKeyboard(view)
            val total = binding.etDes21.text.toString()
            val penalty = binding.etDes22.text.toString()
            val number = binding.etDes23.text.toString()

            if (total.isNotBlank() && number.isNotBlank()) {
                viewModel.setTotal(total)
                viewModel.setPenalty(penalty)
                viewModel.setIsPenalty(viewModel.isPenalty.value!!)
                viewModel.setNumber(number)
                findNavController().navigate(R.id.action_fragment2_to_fragment3)
            } else {
                Toast.makeText(requireContext(), "필수 항목을 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                ConsumptionActivity.hideKeyboard(view)
                binding.etDes21.clearFocus()
                binding.etDes22.clearFocus()
                binding.etDes23.clearFocus()
            }
            false
        }
    }

    private fun updateIsPenaltyButton(isPenalty: Boolean) {
        if(isPenalty) {
            binding.btnPenalty.text = "나의 벌금추가"
        }else{
            binding.btnPenalty.text = "나의 벌금추가"
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        appContainer.calculationConatainer = null
//    }

}