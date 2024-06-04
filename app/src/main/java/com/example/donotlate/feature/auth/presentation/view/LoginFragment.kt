package com.example.donotlate.feature.auth.presentation.view

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.donotlate.MainActivity
import com.example.donotlate.MyApp
import com.example.donotlate.R
import com.example.donotlate.core.presentation.MainFragment
import com.example.donotlate.databinding.FragmentLoginBinding
import com.example.donotlate.feature.auth.presentation.viewmodel.LogInViewModel
import com.example.donotlate.feature.auth.presentation.viewmodel.LogInViewModelFactory

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val logInViewModel: LogInViewModel by activityViewModels {
        val appContainer = (requireActivity().application as MyApp).appContainer
        LogInViewModelFactory(appContainer.authRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        setTitle()
        observedLogInResult()

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startSignUp()
        passwordHide()

        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString().trim()
            val password = binding.etLoginPassword.text.toString().trim()

            logInViewModel.logIn(email, password)

        }
    }


    private fun setTitle() {
        val title = SpannableStringBuilder("환영합니다!\n로그인을 진행해주세요.")
        title.apply {
            setSpan(RelativeSizeSpan(1.1f), 7, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        binding.tvLoginTitle.text = title

    }

    private fun startSignUp() {
        binding.tvLoginSign.setOnClickListener {
            val activity = activity as MainActivity
            activity.changeFragment(SignupFragment())
        }
    }

    private fun passwordHide() {
        val password = binding.etLoginPassword
        val hide = binding.ivLoginHide
        hide.tag = "0"
        hide.setImageResource(R.drawable.hide)
        hide.setOnClickListener {
            when (it.tag) {
                "0" -> {
                    hide.tag = "1"
                    password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    hide.setImageResource(R.drawable.show)
                }

                "1" -> {
                    hide.tag = "0"
                    password.transformationMethod = PasswordTransformationMethod.getInstance()
                    hide.setImageResource(R.drawable.hide)
                }
            }
        }
    }

    private fun observedLogInResult() {
        logInViewModel.logInResult.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                val activity = activity as MainActivity
                activity.changeFragment(MainFragment())
            } else {
                Toast.makeText(requireContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}