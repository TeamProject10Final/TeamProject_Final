package com.nomorelateness.donotlate.feature.auth.presentation.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.databinding.FragmentEmailConfirmDialogBinding
import com.nomorelateness.donotlate.feature.auth.presentation.view.LogInViewModel
import com.nomorelateness.donotlate.feature.auth.presentation.view.LogInViewModelFactory


class EmailConfirmDialogFragment : DialogFragment() {

    private val logInViewModel: LogInViewModel by activityViewModels {
        val appContainer =
            (requireActivity().application as com.nomorelateness.donotlate.DoNotLateApplication).appContainer
        LogInViewModelFactory(
            appContainer.logInUseCase,
            appContainer.checkUserEmailVerificationUseCase,
            appContainer.sendEmailVerificationUseCase,
            appContainer.sessionManager,
            appContainer.deleteUseCase
        )
    }

    private var _binding: FragmentEmailConfirmDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEmailConfirmDialogBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //이메일 재전송
        binding.btnResendEmail.setOnClickListener {
            logInViewModel.sendEmailVerification()
            Toast.makeText(
                requireContext(),
                "E-Mail이 재전송 되었습니다. 메일을 확인해주세요.",
                Toast.LENGTH_LONG
            ).show()
            dismiss()
        }

        //회원탈퇴
        binding.btnWithdrawal.setOnClickListener {
            logInViewModel.deleteUser()
            Toast.makeText(requireContext(), "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        binding.btnBack.setOnClickListener {
            dismiss()
        }
    }

}