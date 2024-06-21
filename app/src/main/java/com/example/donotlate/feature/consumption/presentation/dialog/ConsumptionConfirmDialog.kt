package com.example.donotlate.feature.consumption.presentation.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.donotlate.databinding.BackDialogBinding
import com.example.donotlate.feature.consumption.presentation.ConsumptionModel

class ConsumptionConfirmDialog(
    confirmDialogInterface: ConfirmDialogInterface,
    model: ConsumptionModel
) : DialogFragment() {

    private var _binding: BackDialogBinding? = null
    private val binding get() = _binding!!

    private var confirmDialogInterface: ConfirmDialogInterface? = null
    private var model: ConsumptionModel? = null

    init {
        this.confirmDialogInterface = confirmDialogInterface
        this.model = model
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BackDialogBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        if (model?.isFinished == false) {
            binding.tvDl1.text = "정산을 완료합니다."
            binding.tvDl2.text = "정산 완료로 상태를 변경할까요?"
        } else {
            binding.tvDl1.text = "정산을 취소합니다."
            binding.tvDl2.text = "정산 중으로 상태를 변경할까요?"
        }
        binding.tvDlCancel.setOnClickListener {
            dismiss()
        }
        binding.tvDlConfirm.setOnClickListener {
            this.confirmDialogInterface?.onPositiveButtonClicked(model!!)
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


interface ConfirmDialogInterface {
    fun onPositiveButtonClicked(model: ConsumptionModel)
}