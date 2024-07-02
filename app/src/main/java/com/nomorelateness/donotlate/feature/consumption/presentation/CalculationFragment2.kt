package com.nomorelateness.donotlate.feature.consumption.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.core.util.UtilityKeyboard.UtilityKeyboard.hideKeyboard
import com.nomorelateness.donotlate.databinding.FragmentCalculation2Binding

class CalculationFragment2 : Fragment(R.layout.fragment_calculation2) {

    private var _binding: FragmentCalculation2Binding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels {
        val appContainer =
            (requireActivity().application as com.nomorelateness.donotlate.DoNotLateApplication).appContainer
        SharedViewModelFactory(
            appContainer.insertConsumptionUseCase,
            appContainer.deleteConsumptionUseCase,
            appContainer.getDataCountUseCase,
            appContainer.insertConsumptionDataToFirebaseUseCase
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculation2Binding.inflate(layoutInflater, container, false)

        binding.root.setOnClickListener {
            hideKeyboard(binding.root.windowToken)
//            requireActivity().currentFocus!!.clearFocus()
        }

        return binding.root
    }

    //    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 이전에 입력한 내용이 있다면 해당 내용을 EditText에 설정
        //이부분 수정하기... observe로? 이중인가?
        viewModel.total.value?.let { binding.etDes21.setText(it) }
        viewModel.check3PenaltyStatus()
        viewModel.penalty3Status.observe(viewLifecycleOwner) {
            setPenalty3StatusView(it)
        }

        viewModel.penalty.value?.let { binding.etDes22.setText(it) }
        viewModel.number.value?.let { binding.etDes23.setText(it) }

        binding.btnPenalty.setOnClickListener {
            viewModel.change3PenaltyStatus()
            //viewModel.changeIsPenalty(viewModel.isPenalty.value!!)
//            ConsumptionActivity.hideKeyboard(view)
            hideKeyboard(binding.root.windowToken)
        }

        //이거 프래그먼트별로 해줘야 하는 거 맞나?
        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            Log.d("확인 cal2 에러", "$it")
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
            hideKeyboard(binding.root.windowToken)

            try {

                val total = binding.etDes21.text.toString()
                val penalty = binding.etDes22.text.toString()
                val number = binding.etDes23.text.toString()
                var penaltyNumber = "0"
                if (viewModel.get3PenaltyStatus() != 0) {
                    penaltyNumber = binding.etDes24.text.toString()
                }

                Log.d("확인 Penaltynumber", "${penaltyNumber}, ${(penaltyNumber == "")}")
                Log.d("확인 penalty", "${penalty}, ${(penalty == "")}")

                //시간 되면 이 부분 수정하기... 검사를 뷰모델로 이동해야 함
                if (total.isNotBlank() && number.isNotBlank() && total != "0" && number != "0") {
                    if (viewModel.get3PenaltyStatus() == 0 && penalty.isNotBlank() || viewModel.get3PenaltyStatus() != 0 && penalty.isBlank() || viewModel.get3PenaltyStatus() != 0 && penaltyNumber == "") {
                        Toast.makeText(
                            context,
                            "${resources.getString(R.string.toast_cal_text1)}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("확인 조건문 1", "1")
                    } else if ((penalty.isNotBlank() && penaltyNumber == "") || (viewModel.get3PenaltyStatus() != 0 && penalty.isBlank() && penaltyNumber != "")) {
                        Toast.makeText(
                            context,
                            "${resources.getString(R.string.toast_cal_text1)}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("확인 조건문 1", "2")
                    } else if (penaltyNumber != "" && (penaltyNumber.toInt() >= number.toInt())) {
                        Log.d("확인 조건문 1", "3-1")
                        Toast.makeText(
                            context,
                            "${resources.getString(R.string.toast_cal_text2)}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if ((penalty != "") && (total.toInt() < penalty.toInt())) {
                        Toast.makeText(
                            context,
                            "${resources.getString(R.string.toast_cal_text3)}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        viewModel.setTotal(total)
                        viewModel.setPenalty(penalty)
                        viewModel.setIsPenalty(viewModel.isPenalty.value!!)
                        viewModel.setNumber(number)
                        viewModel.setPenaltyNumber(penaltyNumber)
                        viewModel.setCurrentItem(current = 2)
                        viewModel.calculate()
//                findNavController().navigate(R.id.action_fragment2_to_fragment3)
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "${resources.getString(R.string.cal_frgment_text1)}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
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

    private fun setPenalty3StatusView(status: Int?) {
        when (status) {
            0 -> {
                binding.btnPenalty.setText("${resources.getString(R.string.cal1_frgment_text3)}")
                binding.btnPenalty.setBackgroundResource(R.drawable.btn_info_round_gray)
                binding.etDes24.isVisible = false
                binding.tvDes24.isVisible = false
                binding.tvWon24.isVisible = false
                viewModel.setPenaltyNumber("")
            }

            1 -> {
                //
                binding.btnPenalty.text = "${resources.getString(R.string.cal1_frgment_text2)}"
                binding.btnPenalty.setBackgroundResource(R.drawable.bg_radius_lightblue)
                binding.etDes24.isVisible = true
                binding.tvDes24.isVisible = true
                binding.tvWon24.isVisible = true
            }

            2 -> {
                //
                binding.btnPenalty.text = "${resources.getString(R.string.cal1_frgment_text1)}"
                binding.btnPenalty.setBackgroundResource(R.drawable.btn_radius_lilac)
                binding.etDes24.isVisible = true
                binding.tvDes24.isVisible = true
                binding.tvWon24.isVisible = true
            }

        }

    }


    private fun editTextProcess() {
        binding.etDes23.setOnEditorActionListener { textView, action, keyEvent ->
            var handled = false

            if (action == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(binding.root.windowToken)
                //requireActivity().currentFocus!!.clearFocus()
                handled = true
            }
            handled
        }
    }

    private fun updateIsPenaltyButton(isPenalty: Boolean) {
        if (isPenalty) {
            binding.btnPenalty.text = "${resources.getString(R.string.cal1_frgment_text1)}"
            binding.btnPenalty.setBackgroundResource(R.drawable.btn_radius_lilac)
        } else {
            binding.btnPenalty.text = "${resources.getString(R.string.cal1_frgment_text2)}"
            binding.btnPenalty.setBackgroundResource(R.drawable.bg_radius_lightblue)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}