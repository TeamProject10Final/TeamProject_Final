package com.nomorelateness.donotlate.feature.room.presentation.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.donotlate.R
import com.example.donotlate.databinding.BackDialogBinding
import com.example.donotlate.feature.auth.presentation.view.LoginFragment

class LogoutFragmentDialog() : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        binding.tvDl1.text = "${resources.getString(R.string.dialog_logout_text1)}"
        binding.tvDl2.text = "${resources.getString(R.string.dialog_logout_text2)}"

        binding.tvDlCancel.setOnClickListener {
            setFragmentResult("logoutRequestKey", bundleOf("result" to "cancel"))
            dismiss()
        }
        binding.tvDlConfirm.setOnClickListener {
            setFragmentResult("logoutRequestKey", bundleOf("result" to "confirm"))
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .replace(R.id.frame, LoginFragment())
                .commit()
            dismiss()
        }
    }
}

