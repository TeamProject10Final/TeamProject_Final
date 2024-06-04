package com.example.donotlate.feature.room.presentation.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.donotlate.MainActivity
import com.example.donotlate.databinding.BackDialogBinding
import com.example.donotlate.feature.auth.presentation.view.LoginFragment

class LogoutDialog : DialogFragment() {
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

        binding.tvDl1.text = "로그아웃"
        binding.tvDl2.text = "로그아웃 하시겠습니까?"

        binding.tvDlCancel.setOnClickListener {
            dismiss()
        }
        binding.tvDlConfirm.setOnClickListener {
            val activity = activity as MainActivity
            activity.replaceFragment(LoginFragment())
            dismiss()
        }
    }
}