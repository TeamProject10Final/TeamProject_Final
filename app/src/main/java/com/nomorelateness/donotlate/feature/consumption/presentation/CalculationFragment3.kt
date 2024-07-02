package com.nomorelateness.donotlate.feature.consumption.presentation

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
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.databinding.FragmentCalculation3Binding
import com.nomorelateness.donotlate.feature.consumption.presentation.ConsumptionActivity.Companion.addCommas

class CalculationFragment3 : Fragment(R.layout.fragment_calculation3) {

    private lateinit var binding: FragmentCalculation3Binding
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
                tvRes33.text = "${number} ${resources.getString(R.string.cal3_text4)}"
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
                    binding.tvRes35.text = "${resources.getString(R.string.toast_cal_text4)}"
                    binding.btnCalExit.isVisible = false
                    binding.btnCalFinish.isVisible = false
                }
            }

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