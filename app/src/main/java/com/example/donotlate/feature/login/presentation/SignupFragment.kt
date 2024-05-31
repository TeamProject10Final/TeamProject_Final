package com.example.donotlate.feature.login.presentation

import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.donotlate.MainActivity
import com.example.donotlate.databinding.FragmentSignupBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SignupFragment : Fragment() {

    private lateinit var binding : FragmentSignupBinding

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        setTitle()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startLogin()

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun setTitle() {
        val title = SpannableStringBuilder("회원가입 후\n약속을 잡으러 가볼까요?")
        title.apply {
            setSpan(RelativeSizeSpan(1.5f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.tvSignTitle.text = title

    }

    private fun startLogin() {
        binding.tvSignLogin.setOnClickListener {
            val activity = activity as MainActivity
            activity.changeFragment(LoginFragment())

        }
    }

    private fun checkSingup() {

        val email = binding.etSignEmail
        val password = binding.etSignPassword
        val confirm = binding.etSignConfirm

        email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }
}