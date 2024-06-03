package com.example.donotlate.feature.login.presentation

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.donotlate.MainActivity
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentLoginBinding
import com.example.donotlate.core.presentation.MainFragment

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

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

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startSignUp()
        passwordHide()

        binding.btnLogin.setOnClickListener {
            val activity = activity as MainActivity
            activity.changeFragment(MainFragment())
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
        hide.setImageResource(R.drawable.hide)
        hide.setOnClickListener {
            when(it.tag) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}