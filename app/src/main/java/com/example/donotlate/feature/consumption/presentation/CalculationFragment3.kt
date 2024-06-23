package com.example.donotlate.feature.consumption.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentCalculation3Binding
import com.example.donotlate.feature.consumption.presentation.ConsumptionActivity.Companion.addCommas

class CalculationFragment3 : Fragment(R.layout.fragment_calculation3) {

    private lateinit var binding: FragmentCalculation3Binding
    private val viewModel: SharedViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        SharedViewModelFactory(
            appContainer.insertConsumptionUseCase,
            appContainer.deleteConsumptionUseCase,
            appContainer.getDataCountUseCase
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCalculation3Binding.bind(view)

        //이거 프래그먼트별로 해줘야 하는 거 맞나?
        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            Log.d("확인 cal3 에러", "$it")
        }

        with(binding) {
            viewModel.detail.observe(viewLifecycleOwner) { detail ->
                tvRes31.text = detail ?: ""
                tvRes31.text = detail
            }
            viewModel.date.observe(viewLifecycleOwner) { date ->
                tvRes32.text = date ?: ""
            }
            viewModel.number.observe(viewLifecycleOwner) { number ->
                tvRes33.text = "${number} 명"
            }
            viewModel.category.observe(viewLifecycleOwner) { category ->
                tvRes34.text = category ?: ""
            }

            viewModel.calResult.observe(viewLifecycleOwner) {
                if (it != 0) {
                    binding.tvRes35.text = it.addCommas()
                    binding.btnCalExit.isVisible = true
                    binding.btnCalFinish.isVisible = true
                } else {
                    binding.tvRes35.text = "다시 정산해 주세요."
                    binding.btnCalExit.isVisible = false
                    binding.btnCalFinish.isVisible = false
                }
            }


//하단 삭제
//            viewModel.mediatorLiveData.observe(viewLifecycleOwner) {
//                calculate()?.let {
//                    binding.tvRes35.text = it.addCommas()
//                    viewModel.setPrice(it)
//                    Log.d("확인", "calculate() 실행")
//                }
//            }




            btnCalExit.setOnClickListener {
                handleSaveConsumption(false)
            }
            btnCalFinish.setOnClickListener {
                handleSaveConsumption(true)
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)


    }


    override fun onResume() {
        super.onResume()

//        Log.d("3번 페이지 데이터 확인", "${viewModel.total.value}, ${viewModel.number.value}, ${viewModel.penalty.value}, ${viewModel.isPenalty.value}")
//        viewModel.price.observe(viewLifecycleOwner, { price ->
//            Log.d("확인","viewModel.price.observe 실행")
//            if (binding.tvRes35.text.isNullOrEmpty())
//            calculate()?.let {
//                binding.tvRes35.text = it.addCommas()
//                viewModel.setPrice(it)
//                Log.d("확인","calculate() 실행")
//            }
//        })
    }

    private fun calculate(): Int? {

        val total = viewModel.total.value?.toIntOrNull() ?: return null
        val number = viewModel.number.value?.toIntOrNull() ?: return null
        val penaltyString = viewModel.penalty.value
        val isPenalty = viewModel.isPenalty.value

        // penalty가 빈칸이거나 null인 경우 0으로 간주하여 처리...
        val penalty = penaltyString?.takeIf { it.isNotBlank() }?.toIntOrNull() ?: 0

        // number가 0인 경우에는 0으로 나누는 오류가 발생하므로 예외 처리하기
        if (number == 0) {
            return null
        }

        if (isPenalty == true && penalty != 0) {
            val result = ((total - penalty) / number) + penalty
            return result ?: 0
        } else {
            val result = (total - penalty) / number
            return result ?: 0
        }
//        }
    }

    private fun handleSaveConsumption(isFinished: Boolean) {
        viewModel.setIsFinished(isFinished)
        viewModel.saveConsumption(requireContext())
        activity?.let {
            it.finish() // 현재 액티비티를 종료
            val intent = Intent(it, ConsumptionActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}