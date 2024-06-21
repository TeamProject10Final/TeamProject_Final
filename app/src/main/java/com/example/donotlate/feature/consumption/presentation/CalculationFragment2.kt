package com.example.donotlate.feature.consumption.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.core.util.UtilityKeyboard.UtilityKeyboard.hideKeyboard
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

        binding.root.setOnClickListener {
            hideKeyboard()
            requireActivity().currentFocus!!.clearFocus()
        }

        return binding.root
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 이전에 입력한 내용이 있다면 해당 내용을 EditText에 설정
        viewModel.total.value?.let { binding.etDes21.setText(it) }
        viewModel.isPenalty.value?.let {
            if (it) binding.btnPenalty.setText("${resources.getString(R.string.cal1_frgment_text1)}") else binding.btnPenalty.setText(
                "${resources.getString(R.string.cal1_frgment_text2)}"
            )
        }
        viewModel.penalty.value?.let { binding.etDes22.setText(it) }
        viewModel.number.value?.let { binding.etDes23.setText(it) }

        binding.btnPenalty.setOnClickListener {
            viewModel.changeIsPenalty(viewModel.isPenalty.value!!)
//            ConsumptionActivity.hideKeyboard(view)
            hideKeyboard()
        }

        viewModel.isPenalty.observe(viewLifecycleOwner){isPenalty ->
            updateIsPenaltyButton(isPenalty)
        }

//        binding.etDes21.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
//            if (!hasFocus) {
//                binding.etDes21.clearFocus()
//                ConsumptionActivity.hideKeyboard(view)
//            }
//        }
//
//        binding.etDes22.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
//            if (!hasFocus) {
//                binding.etDes22.clearFocus()
//                ConsumptionActivity.hideKeyboard(view)
//            }
//        }
        editTextProcess()

//        binding.etDes23.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
//            if (!hasFocus) {
//                binding.etDes23.clearFocus()
//                ConsumptionActivity.hideKeyboard(view)
//            }
//        }

        binding.btnCalEnd.setOnClickListener {
//            ConsumptionActivity.hideKeyboard(view)
            hideKeyboard()
            val total = binding.etDes21.text.toString()
            val penalty = binding.etDes22.text.toString()
            val number = binding.etDes23.text.toString()

            if (total.isNotBlank() && number.isNotBlank() && total != "0") {
                viewModel.setTotal(total)
                viewModel.setPenalty(penalty)
                viewModel.setIsPenalty(viewModel.isPenalty.value!!)
                viewModel.setNumber(number)
                viewModel.setCurrentItem(current = 2)
//                findNavController().navigate(R.id.action_fragment2_to_fragment3)
            } else {
                Toast.makeText(
                    requireContext(),
                    "${resources.getString(R.string.cal_frgment_text1)}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

//        binding.root.setOnTouchListener { _, event ->
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                ConsumptionActivity.hideKeyboard(view)
//                binding.etDes21.clearFocus()
//                binding.etDes22.clearFocus()
//                binding.etDes23.clearFocus()
//            }
//            false
//        }
    }


    private fun editTextProcess() {
        binding.etDes23.setOnEditorActionListener { textView, action, keyEvent ->
            var handled = false

            if (action == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                requireActivity().currentFocus!!.clearFocus()
                handled = true
            }
            handled
        }
    }

    private fun updateIsPenaltyButton(isPenalty: Boolean) {
        if(isPenalty) {
            binding.btnPenalty.text = "${resources.getString(R.string.cal1_frgment_text1)}"
        }else{
            binding.btnPenalty.text = "${resources.getString(R.string.cal1_frgment_text2)}"
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