package com.example.donotlate.feature.mypromise.presentation.view.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.example.donotlate.databinding.DialogRadiobuttonSelectionBinding

class RadioButtonSelectionDialog(
    private val selections: List<String>,
    private val callback: (Int) -> Unit
) : DialogFragment() {

//    private val myPromiseViewModel: MyPromiseRoomViewModel by activityViewModels {
//        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
//        MyPromiseRoomViewModelFactory(
//            appContainer.messageSendingUseCase,
//            appContainer.messageReceivingUseCase,
//            appContainer.getDirectionsUseCase,
//            appContainer.removeParticipantsUseCase,
//            appContainer.updateArrivalStatusUseCase
//        )
//    }

    private var _binding: DialogRadiobuttonSelectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogRadiobuttonSelectionBinding.inflate(LayoutInflater.from(context))
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            if (selections.isEmpty()) {
                Toast.makeText(context, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
                dismiss()
                return builder.create()
            }

            val radioButtons = listOf(
                binding.rbFirst,
                binding.rbSecond,
                binding.rbThird,
                binding.rbFourth,
                binding.rbFifth,
                binding.rbSixth
            )
            val lines = listOf(
                binding.lineFirst,
                binding.lineSecond,
                binding.lineThird,
                binding.lineFourth,
                binding.lineFifth
            )

            selections.forEachIndexed { index, selection ->
                if (index >= 1) {
                    lines[index - 1].apply {
                        isVisible = true
                    }
                }
                radioButtons[index].apply {
                    text = selection
                    isVisible = true
                }

                radioButtons[index].apply {

                }
            }

            binding.btnRadioConfirm01.setOnClickListener {
                val selectedId = binding.radioGroup01.checkedRadioButtonId
                val selectedIndex =
                    binding.radioGroup01.indexOfChild(binding.root.findViewById(selectedId))
                Log.d("확인 SelectedIndex", "${selectedIndex}")
                if (selectedIndex != -1) {
                    //myPromiseViewModel.setSelectedRouteIndex(selectedId)
                    callback(selectedIndex / 2)
                    dismiss()
                }
            }
            binding.btnRadioCancel01.setOnClickListener {
                dismiss()
            }

            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}