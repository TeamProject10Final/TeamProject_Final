package com.example.donotlate.feature.mypromise.presentation.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.databinding.DialogRadiobuttonBinding
import com.example.donotlate.feature.mypromise.presentation.view.MyPromiseRoomViewModel
import com.example.donotlate.feature.mypromise.presentation.view.MyPromiseRoomViewModelFactory

class RadioButtonDialog : DialogFragment() {

    private val myPromiseViewModel: MyPromiseRoomViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        MyPromiseRoomViewModelFactory(
            appContainer.messageSendingUseCase,
            appContainer.messageReceivingUseCase,
            appContainer.getDirectionsUseCase
        )
    }

    private var _binding: DialogRadiobuttonBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogRadiobuttonBinding.inflate(inflater, container, false)

        isCancelable = false
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        binding.radioGroup.setOnCheckedChangeListener { _, check ->
//            binding.btnRadioConfirm.setOnClickListener {
//                when (check) {
//                    R.id.rb_Transit -> myPromiseViewModel.setMode(check)
//                    R.id.rb_Driving -> myPromiseViewModel.setMode(check)
//                    R.id.rb_Walking -> myPromiseViewModel.setMode(check)
//                    R.id.rb_Bicycling -> myPromiseViewModel.setMode(check)
//                }
//                dismiss()
//            }
//        }

        binding.btnRadioCancel.setOnClickListener {
            dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}