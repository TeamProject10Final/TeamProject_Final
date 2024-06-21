package com.example.donotlate.feature.auth.presentation.view

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.core.util.UtilityKeyboard.UtilityKeyboard.hideKeyboard
import com.example.donotlate.databinding.FragmentLoginBinding
import com.example.donotlate.feature.main.presentation.view.MainFragment
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val logInViewModel: LogInViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        LogInViewModelFactory(appContainer.logInUseCase)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)
        initViews()
        collectFlows()

        binding.root.setOnClickListener {
            hideKeyboard()
            requireActivity().currentFocus!!.clearFocus()
        }
        editTextProcess()
    }

    private fun editTextProcess() {
        binding.etLoginPassword.setOnEditorActionListener { textView, action, keyEvent ->
            var handled = false

            if (action == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                requireActivity().currentFocus!!.clearFocus()
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
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                logInViewModel.channel.collect {
                    when (it) {
                        LoginEvent.LoginFail -> Toast.makeText(
                            requireContext(),
                            "로그인에 실패하였습니다.",
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
                    }
                }
            }
        }
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
                            binding.ivLoginHide.setImageResource(R.drawable.hide)
                        }

                        else -> Unit
                    }
                }

                else -> Unit
            }
        }
    }
}