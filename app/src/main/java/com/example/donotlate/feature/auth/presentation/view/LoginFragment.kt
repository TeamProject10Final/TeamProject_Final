package com.example.donotlate.feature.auth.presentation.view

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.MainActivity
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentLoginBinding
import com.example.donotlate.feature.auth.presentation.viewmodel.LogInViewModel
import com.example.donotlate.feature.auth.presentation.viewmodel.LogInViewModelFactory
import com.example.donotlate.feature.main.presentation.view.MainFragment
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private val auth by lazy{
        FirebaseAuth.getInstance()
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val logInViewModel: LogInViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        LogInViewModelFactory(appContainer.logInUseCase)
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

//        setTitle()
        observedLogInResult()


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("FirebaseAuth", "${auth.currentUser?.uid}")
        startSignUp()
        passwordHide()

        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString().trim()
            val password = binding.etLoginPassword.text.toString().trim()

            logInViewModel.logIn(email, password)

        }
    }


    private fun startSignUp() {
        binding.tvLoginSign.setOnClickListener {
            val activity = activity as MainActivity
            activity.supportFragmentManager.beginTransaction()
                .add(R.id.fragment_Login, SignupFragment())
                .commit()
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
                activity.replaceFragment(MainFragment())
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