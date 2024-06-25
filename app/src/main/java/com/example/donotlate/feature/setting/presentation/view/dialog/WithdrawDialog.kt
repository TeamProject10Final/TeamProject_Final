package com.example.donotlate.feature.setting.presentation.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.BackDialogBinding
import com.example.donotlate.feature.auth.presentation.view.LoginFragment
import com.example.donotlate.feature.setting.presentation.view.SettingsViewModel
import com.example.donotlate.feature.setting.presentation.view.SettingsViewModelFactory
import kotlin.concurrent.thread

class WithdrawDialog(
    userInterface: UserInterface,
    userId: String
) : DialogFragment() {

    private var userId: String? = null
    private var userInterface: UserInterface

    init {
        this.userId = userId
        this.userInterface = userInterface
    }


    private val viewModel: SettingsViewModel by viewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        SettingsViewModelFactory(
            sessionManager = appContainer.sessionManager,
            deleteUserUseCase = appContainer.deleteUserUseCase
        )
    }

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

        isCancelable = true
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDl1.text = "정말 탈퇴 하시겠어요?\uD83D\uDE2D"
        binding.tvDl2.text = "확인을 누르시면 회원 탈퇴가 됩니다."

        binding.tvDlCancel.setOnClickListener {
            dismiss()
        }
        binding.tvDlConfirm.setOnClickListener {

            val loading = LoadingDialog()
            loading.show(childFragmentManager, "tag")
            thread(start = true) {
                Thread.sleep(2000)
                this.userInterface?.onClickUserButton(userId!!)
                dismiss()
            }
        }
    }
}

interface UserInterface {
    fun onClickUserButton(userId: String)
}