package com.nomorelateness.donotlate.feature.consumption.presentation

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.core.util.UtilityKeyboard.UtilityKeyboard.hideKeyboard
import com.nomorelateness.donotlate.databinding.FragmentCalculation1Binding
import com.nomorelateness.donotlate.feature.room.presentation.dialog.DatePickerInterface
import com.nomorelateness.donotlate.feature.room.presentation.dialog.RoomDateDialog
import java.util.Calendar

class CalculationFragment1 : Fragment(R.layout.fragment_calculation1), DatePickerInterface {
    private var _binding: FragmentCalculation1Binding? = null
    private val binding get() = _binding!!

    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0


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
        _binding = FragmentCalculation1Binding.inflate(layoutInflater, container, false)

        binding.root.setOnClickListener {
            hideKeyboard(binding.root.windowToken)
//            requireActivity().currentFocus!!.clearFocus()
        }

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinner()

        // 이전에 입력한 내용이 있다면 해당 내용을 EditText에 설정
        viewModel.detail.value?.let { binding.etDes11.setText(it) }
        viewModel.date.value?.let { binding.etDes12.text = it }
        // 이전에 입력한 카테고리 설정
        viewModel.category.value?.let { category ->
            val categories = resources.getStringArray(R.array.consumptionCategoryArray)
            val categoryIndex = categories.indexOf(category)
            if (categoryIndex >= 0 && categoryIndex < binding.consumptionSpinner.adapter.count) {
                binding.consumptionSpinner.setSelection(categoryIndex)
            }
        }

        binding.ivDate.setOnClickListener {
            showDatePickerDialog()
        }

        binding.etDes11.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                binding.etDes11.clearFocus()
//                ConsumptionActivity.hideKeyboard(v)
                hideKeyboard(binding.root.windowToken)
            }
        }

        showDatePickerDialog()


        binding.consumptionSpinner.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
//                ConsumptionActivity.hideKeyboard(view)
                hideKeyboard(binding.root.windowToken)
            }
            false
        }

        binding.btnCalNext.setOnClickListener {
//            ConsumptionActivity.hideKeyboard(view)
            hideKeyboard(binding.root.windowToken)
            val detail = binding.etDes11.text.toString()
            val date = binding.etDes12.text.toString()
            val category =
                if (binding.consumptionSpinner.selectedItemPosition != binding.consumptionSpinner.adapter.count - 1) {
                    binding.consumptionSpinner.selectedItem.toString()

                } else {
                    ""
                }
            if (detail.isNotBlank() && date.isNotBlank() && category.isNotBlank()) {
                viewModel.setDetail(detail)
                viewModel.setDate(date)
                viewModel.setCategory(category)
                viewModel.setCurrentItem(current = 1)
                //findNavController().navigate(R.id.action_fragment1_to_fragment2)
            } else {
                Toast.makeText(
                    requireContext(),
                    "${resources.getString(R.string.cal_frgment_text1)}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        //바깥 터치 시 키보드 숨기는 부분...
//        binding.root.setOnTouchListener { _, event ->
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                ConsumptionActivity.hideKeyboard(view)
//                binding.etDes11.clearFocus()
//                binding.etDes12.clearFocus()
//                binding.consumptionSpinner.clearFocus()
//            }
//            false
//        }
        //이거 프래그먼트별로 해줘야 하는 거 맞나?
        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            Log.d("확인 cal1 에러", "$it")
        }

    }

    private fun setupSpinner() {
        val categories = resources.getStringArray(R.array.consumptionCategoryArray)
        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        ) {
            override fun getCount(): Int {
                // Hint를 제외한 실제 항목의 수 반환하려고 1을 뺌
                return super.getCount() - 1
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.consumptionSpinner.adapter = adapter
        binding.consumptionSpinner.setSelection(adapter.count)  // 힌트로 설정... 근데 그냥 공백으로 바꿈

        binding.consumptionSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // 힌트를 고른(아무것도 고르지 않은) 경우 아무것도 하지 않음...
                    if (position == adapter.count) {
                        (view as TextView).setTextColor(Color.GRAY)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    //
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDatePickerDialog() {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val getMonth = if (month + 1 < 10) "0${month + 1}" else month + 1
        val getDay = if (day < 10) "0${day}" else day

        binding.etDes12.text = "${year}-${getMonth}-${getDay}"

        binding.etDes12.setOnClickListener {
            val dialog = RoomDateDialog(this, mYear, mMonth, mDay)
            dialog.show(childFragmentManager, "tag")
//            ConsumptionActivity.hideKeyboard(view)
            hideKeyboard(binding.root.windowToken)
        }


//        val c = Calendar.getInstance()
//        val year = c.get(Calendar.YEAR)
//        val month = c.get(Calendar.MONTH)
//        val day = c.get(Calendar.DAY_OF_MONTH)
//
//        val datePickerDialog = DatePickerDialog(
//            requireContext(),
//            { _, selectedYear, selectedMonth, selectedDay ->
//                val formattedYear = selectedYear % 100
//                val selectedDate = String.format("%02d-%02d-%02d", formattedYear, selectedMonth + 1, selectedDay)
//                binding.etDes12.setText(selectedDate)
//            },
//            year,
//            month,
//            day
//        )
//
//        datePickerDialog.show()
    }

    override fun onClickDateButton(year: Int, month: Int, day: Int) {
        mYear = year
        mMonth = month
        mDay = day

        val getMonth = if (month + 1 < 10) "0${month + 1}" else month + 1
        val getDay = if (day < 10) "0${day}" else day
        val result = "${year}-${getMonth}-${getDay}"
        binding.etDes12.text = result
    }

}