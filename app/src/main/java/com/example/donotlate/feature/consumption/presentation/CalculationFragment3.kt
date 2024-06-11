package com.example.donotlate.feature.consumption.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.feature.consumption.presentation.ConsumptionActivity.Companion.addCommas
import com.example.donotlate.databinding.FragmentCalculation3Binding

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

        with(binding) {
            viewModel.detail.observe(viewLifecycleOwner, { detail ->
                tvRes31.text = detail ?: ""
                tvRes31.setText(detail)
            })
            viewModel.date.observe(viewLifecycleOwner, { date ->
                tvRes32.text = date ?: ""
            })
            viewModel.number.observe(viewLifecycleOwner, { number ->
                tvRes33.text = "$number 명"
            })
            viewModel.category.observe(viewLifecycleOwner, { category ->
                tvRes34.text = category ?: ""
            })
            calculate()?.let {
                tvRes35.text = it.addCommas()
                viewModel.setPrice(it)
            }

            btnCalExit.setOnClickListener {
                handleSaveConsumption(false)
            }
            btnCalFinish.setOnClickListener {
                handleSaveConsumption(true)
            }
        }


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
            Log.d("penalty", "00")
            return result ?: 0
        } else {
            val result = (total - penalty) / number
            Log.d("penalty", "xx")
            return result ?: 0
        }
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


