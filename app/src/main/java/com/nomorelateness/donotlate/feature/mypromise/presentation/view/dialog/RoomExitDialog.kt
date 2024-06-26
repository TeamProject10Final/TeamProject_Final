package com.nomorelateness.donotlate.feature.mypromise.presentation.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.nomorelateness.donotlate.databinding.BackDialogBinding

class RoomExitDialog(
    roomExitInterface: RoomExitInterface,
) : DialogFragment() {


    private var _binding: BackDialogBinding? = null
    private val binding get() = _binding!!

    private var roomExitInterface: RoomExitInterface? = null

    init {
        this.roomExitInterface = roomExitInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BackDialogBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDl1.text = "나가기"
        binding.tvDl2.text = "정말 채팅방에서 나가시겠습니까??"

        binding.tvDlCancel.setOnClickListener {
            dismiss()
        }
        binding.tvDlConfirm.setOnClickListener {
            this.roomExitInterface?.onClickExitRoom()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


interface RoomExitInterface {
    fun onClickExitRoom()
}