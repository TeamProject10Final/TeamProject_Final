package com.nomorelateness.donotlate.feature.auth.presentation.view

import SignUpEvent
import SignUpViewModel
import SignUpViewmodelFactory
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.core.presentation.util.UtilityKeyboard.UtilityKeyboard.hideKeyboard
import com.nomorelateness.donotlate.databinding.FragmentSignupBinding
import com.nomorelateness.donotlate.feature.auth.presentation.dialog.InformationDialogFragment
import kotlinx.coroutines.launch

class SignupFragment : Fragment(R.layout.fragment_signup), View.OnClickListener {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    // SignUpViewModel 초기화
    private val signUpViewModel: SignUpViewModel by viewModels {
        val appContainer =
            (requireActivity().application as com.nomorelateness.donotlate.DoNotLateApplication).appContainer
        SignUpViewmodelFactory(appContainer.signUpUseCase, appContainer.sessionManager)
    }

    // onViewCreated를 재정의하여 뷰와 옵저버 설정
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignupBinding.bind(view)
        initViews()
        collectFlows()

        binding.root.setOnClickListener {
            hideKeyboard(binding.root.windowToken)
        }
        editTextProcess()
        infoDialog()
    }

    // 뷰 초기화 및 클릭 리스너 설정
    private fun initViews() {
        binding.tvSignLogin.setOnClickListener(this)
        binding.btnSignUp.setOnClickListener(this)
        binding.ivSignHide.setOnClickListener(this)

        // 비밀번호 숨김 버튼 초기 상태 설정
        binding.ivSignHide.tag = "0"
        binding.ivSignHide.setImageResource(R.drawable.ic_hide)
    }

    // EditText 액션 리스너 설정
    private fun editTextProcess() {
        binding.etSignName.doAfterTextChanged {
            signUpViewModel.checkName(binding.etSignName, binding.tvNameCheck)
        }
        binding.etSignEmail.doAfterTextChanged {
            signUpViewModel.checkEmail(binding.etSignEmail, binding.tvEmailCheck)
        }
        binding.etSignPassword.doAfterTextChanged {
            signUpViewModel.checkPw(binding.etSignPassword, binding.tvPasswordCheck)
        }
        binding.etSignConfirm.doAfterTextChanged {
            signUpViewModel.checkConfirmPw(
                binding.etSignPassword,
                binding.etSignConfirm,
                binding.tvConfirmCheck
            )
        }
        binding.etSignConfirm.setOnEditorActionListener { _, action, _ ->
            var handled = false
            if (action == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(binding.root.windowToken)
                handled = true
            }
            handled
        }
    }

    // Flows를 수집하고 유효성 검사 결과를 관찰
    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                signUpViewModel.eventFlow.collect {
                    when (it) {
                        is SignUpEvent.SignUpSuccess -> {
                            Toast.makeText(
                                requireContext(),
                                resources.getString(R.string.toast_login_text2),
                                Toast.LENGTH_SHORT
                            ).show()
                            parentFragmentManager.beginTransaction()
                                .setCustomAnimations(
                                    R.anim.fade_in,
                                    R.anim.fade_out
                                )
                                .replace(R.id.frame, LoginFragment())
                                .commit()
                        }

                        is SignUpEvent.SignUpFail -> {
                            Toast.makeText(
                                requireContext(),
                                resources.getString(R.string.toast_login_text3),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun infoDialog() {
        val dialog = InformationDialogFragment()
        dialog.show(requireActivity().supportFragmentManager, "InformationDialogFragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it) {
                // 회원가입 버튼 클릭 시
                binding.btnSignUp -> {

                    signUpViewModel.checkSignUp(
                        binding.etSignName,
                        binding.etSignEmail,
                        binding.etSignPassword,
                        binding.etSignConfirm,
                        binding.tvNameCheck,
                        binding.tvEmailCheck,
                        binding.tvPasswordCheck,
                        binding.tvConfirmCheck,
                        it
                    )
                    hideKeyboard(binding.root.windowToken)
                }

                // 로그인 텍스트 클릭 시
                binding.tvSignLogin -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out
                        )
                        .replace(R.id.frame, LoginFragment())
                        .addToBackStack("LoginFragment")
                        .commit()
                }

                // 비밀번호 표시/숨기기 아이콘 클릭 시
                binding.ivSignHide -> {
                    if (binding.ivSignHide.tag == "0") {
                        binding.ivSignHide.tag = "1"
                        binding.etSignPassword.transformationMethod =
                            HideReturnsTransformationMethod.getInstance()
                        binding.ivSignHide.setImageResource(R.drawable.show)
                    } else {
                        binding.ivSignHide.tag = "0"
                        binding.etSignPassword.transformationMethod =
                            PasswordTransformationMethod.getInstance()
                        binding.ivSignHide.setImageResource(R.drawable.ic_hide)
                    }
                }

                else -> Unit
            }
        }
    }
}