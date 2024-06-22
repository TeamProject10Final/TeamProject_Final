package com.example.donotlate.feature.mypromise.presentation.view.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.DialogRadiobuttonBinding
import com.example.donotlate.feature.mypromise.presentation.model.Mode
import com.example.donotlate.feature.mypromise.presentation.view.MyPromiseRoomViewModel
import com.example.donotlate.feature.mypromise.presentation.view.MyPromiseRoomViewModelFactory

class RadioButtonDialog(private val callback: () -> Unit) :
    DialogFragment() {

    private val myPromiseViewModel: MyPromiseRoomViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        MyPromiseRoomViewModelFactory(
            appContainer.messageSendingUseCase,
            appContainer.messageReceivingUseCase,
            appContainer.getDirectionsUseCase,
            appContainer.removeParticipantsUseCase
        )
    }

    private var _binding: DialogRadiobuttonBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogRadiobuttonBinding.inflate(LayoutInflater.from(context))
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val modeArray = resources.getStringArray(R.array.modeArray)
            binding.rbTransit.text = modeArray[0]
            binding.rbDriving.text = modeArray[1]
            binding.rbWalking.text = modeArray[2]
            binding.rbBicycling.text = modeArray[3]

            binding.btnRadioConfirm.setOnClickListener {
                val selectedId = binding.radioGroup.checkedRadioButtonId
                val selectedKey = when (selectedId) {
                    R.id.rb_Transit -> Mode.OPTION_1.key
                    R.id.rb_Driving -> Mode.OPTION_2.key
                    R.id.rb_Walking -> Mode.OPTION_3.key
                    R.id.rb_Bicycling -> Mode.OPTION_4.key
                    else -> null
                }
                selectedKey?.let { key ->
                    myPromiseViewModel.setMode(key)
                    callback()
                    dismiss()
                }
            }
            binding.btnRadioCancel.setOnClickListener {
                dismiss()
            }

            isCancelable = false
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}