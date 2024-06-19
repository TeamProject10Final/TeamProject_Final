package com.example.donotlate.feature.auth.presentation.view

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentLoginBinding
import com.example.donotlate.feature.auth.presentation.viewmodel.LogInViewModel
import com.example.donotlate.feature.auth.presentation.viewmodel.LogInViewModelFactory
import com.example.donotlate.feature.auth.presentation.viewmodel.LoginEvent
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
                        .replace(R.id.fragment_Login, SignupFragment())
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