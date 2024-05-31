package com.example.donotlate.feature.login.presentation

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.donotlate.MainActivity
import com.example.donotlate.databinding.FragmentLoginBinding
import com.example.donotlate.core.presentation.MainFragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

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
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        setTitle()

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startSignUp()

        binding.btnLogin.setOnClickListener {
            val activity = activity as MainActivity
            activity.changeFragment(MainFragment())
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun setTitle() {
        val title = SpannableStringBuilder("환영합니다!\n로그인을 진행해주세요.")
        title.apply {
            setSpan(RelativeSizeSpan(1.5f), 7, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        binding.tvLoginTitle.text = title

    }

    private fun startSignUp() {
        binding.tvLoginSign.setOnClickListener {
            val activity = activity as MainActivity
            activity.changeFragment(SignupFragment())
        }
    }
}