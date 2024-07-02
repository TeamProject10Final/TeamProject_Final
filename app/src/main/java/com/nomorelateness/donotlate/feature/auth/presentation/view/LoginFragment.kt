package com.nomorelateness.donotlate.feature.auth.presentation.view

import android.content.Context
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.core.util.UtilityKeyboard.UtilityKeyboard.hideKeyboard
import com.nomorelateness.donotlate.databinding.FragmentLoginBinding
import com.nomorelateness.donotlate.feature.auth.presentation.dialog.EmailConfirmDialogFragment
import com.nomorelateness.donotlate.feature.main.presentation.view.MainFragment
import com.nomorelateness.donotlate.feature.tutorial.TutorialViewPagerFragment
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)
        initViews()

        binding.root.setOnClickListener {
            hideKeyboard(binding.root.windowToken)
//            requireActivity().currentFocus!!.clearFocus()
        }
        editTextProcess()
    }

    private fun editTextProcess() {
        binding.etLoginPassword.setOnEditorActionListener { textView, action, keyEvent ->
            var handled = false

            if (action == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(binding.root.windowToken)
                //requireActivity().currentFocus!!.clearFocus()
                handled = true
            }
            handled
        }
    }

    private fun initViews() {
        binding.tvLoginSign.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        binding.ivLoginHide.setOnClickListener(this)
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                logInViewModel.channel.collect {
                    when (it) {
                        LoginEvent.LoginFail -> Toast.makeText(
                            requireContext(),
                            "${resources.getString(R.string.toast_login_text1)}",
                            Toast.LENGTH_SHORT
                        ).show()

                        LoginEvent.LoginSuccess -> {
                            parentFragmentManager.beginTransaction()
                                .setCustomAnimations(
                                    R.anim.fade_in,
                                    R.anim.fade_out
                                )
                                .replace(R.id.frame, MainFragment())
                                .commit()
                        }

                        LoginEvent.EmailNotVerified -> {
                            showEmailVerificationDialog()
                        }
                    }
                }
            }
        }
    }

    fun showEmailVerificationDialog() {
        EmailConfirmDialogFragment().show(childFragmentManager, "EmailConfirmDialogFragment")
//        AlertDialog.Builder(requireContext())
//            .setTitle("이메일 인증을 진행해주세요.")
//            .setMessage("이메일 인증이 필요합니다.\n입력하신 email로 전송된 인증을 진행해 주세요.\n\n인증을 원치 않으시면 회원탈퇴를 눌러주세요.")
//            .setPositiveButton("재전송") { _, _ ->
//                val email = binding.etLoginEmail.text.toString()
//                val password = binding.etLoginPassword.text.toString()
//                logInViewModel.sendEmailVerification()
//                Toast.makeText(
//                    requireContext(),
//                    "E-Mail이 재전송 되었습니다. 메일을 확인해주세요.",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//            .setNegativeButton("회원 탈퇴") { _, _ ->
//                logInViewModel.deleteUser()
//
//                Toast.makeText(requireContext(), "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
//            }
//            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it) {
                binding.btnLogin -> {
                    val email = binding.etLoginEmail.text.toString()
                    val password = binding.etLoginPassword.text.toString()
                    logInViewModel.logIn(email = email, password = password)
                    checkFirst()
                    hideKeyboard(binding.root.windowToken)
                }

                binding.tvLoginSign -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            /* enter = */ R.anim.slide_in,
                            /* exit = */ R.anim.fade_out
                        )
                        .replace(R.id.frame, SignupFragment())
                        .addToBackStack("SignupFragment")
                        .commit()
                }

                binding.ivLoginHide -> {
                    when (it.tag) {
                        "0" -> {
                            binding.ivLoginHide.tag = "1"
                            binding.etLoginPassword.transformationMethod =
                                HideReturnsTransformationMethod.getInstance()
                            binding.ivLoginHide.setImageResource(R.drawable.show)
                        }

                        "1" -> {
                            binding.ivLoginHide.tag = "0"
                            binding.etLoginPassword.transformationMethod =
                                PasswordTransformationMethod.getInstance()
                            binding.ivLoginHide.setImageResource(R.drawable.ic_hide)
                        }

                        else -> Unit
                    }
                }

                else -> Unit
            }
        }
    }

    private fun checkFirst() {
        val sharedPref =
            activity?.getSharedPreferences("checkFirst", Context.MODE_PRIVATE) ?: return
        val sharedprefValue = sharedPref.getBoolean("checkFirst", false)

        if (!sharedprefValue) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame, TutorialViewPagerFragment())
                .commit()
            with(sharedPref.edit()) {
                putBoolean("checkFirst", true)
                apply()

            }
        } else {
            collectFlows()
        }
    }
}