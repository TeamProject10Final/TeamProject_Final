package com.example.donotlate.feature.room.presentation.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.donotlate.R
import com.example.donotlate.databinding.BackDialogBinding
import com.example.donotlate.feature.room.presentation.view.RoomActivity
import com.example.donotlate.feature.room.presentation.view.RoomResultFragment

class ResultFragmentDialog : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = true
    }

    private lateinit var binding: BackDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BackDialogBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDl1.text = "수정이 불가능해요"
        binding.tvDl2.text = "확인을 누르시면 수정이 불가능합니다!\n정말 이대로 진행할까요?"

        binding.tvDlCancel.setOnClickListener {
            dismiss()
        }
        binding.tvDlConfirm.setOnClickListener {
            val activity = activity as RoomActivity
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.frame_room, RoomResultFragment()).commit()
            dismiss()
        }
    }
}