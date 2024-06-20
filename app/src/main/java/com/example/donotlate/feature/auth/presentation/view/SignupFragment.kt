package com.example.donotlate.feature.auth.presentation.view

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentSignupBinding
import com.example.donotlate.feature.auth.presentation.dialog.InformationDialogFragment
import com.example.donotlate.feature.main.presentation.view.MainFragment

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val signUpViewmodel: SignUpViewModel by viewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        SignUpViewmodelFactory(appContainer.signUpUseCase)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startLogin()
        checkSingup()
        passwordHide()
        checkedSignUpResult()
        infoDialog()

        binding.btnSignUp.setOnClickListener {
            clickToSignUpButton()
        }
    }


    private fun startLogin() {
        binding.tvSignLogin.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .remove(this)
                .commit()
        }
    }

    private fun checkSingup() {

        val name = binding.etSignName
        val email = binding.etSignEmail
        val password = binding.etSignPassword
        val confirm = binding.etSignConfirm

        val nameCheck = binding.tvNameCheck
        val emailCheck = binding.tvEmailCheck
        val passwordCheck = binding.tvPasswordCheck
        val confirmCheck = binding.tvConfirmCheck


        name.doAfterTextChanged { signUpViewmodel.checkName(name, nameCheck) }
        email.doAfterTextChanged { signUpViewmodel.checkEmail(email, emailCheck) }
        password.doAfterTextChanged { signUpViewmodel.checkPw(password, passwordCheck) }
        confirm.doAfterTextChanged {
            signUpViewmodel.checkConfirmPw(
                password,
                confirm,
                confirmCheck
            )
        }



        binding.btnSignUp.setOnClickListener {

            signUpViewmodel.checkSignUp(
                name,
                email,
                password,
                confirm,
                nameCheck,
                emailCheck,
                passwordCheck,
                confirmCheck,
                it
            )
            requireActivity().supportFragmentManager.beginTransaction()
                .remove(this)
                .commit()
        }
    }

    private fun passwordHide() {
        val password = binding.etSignPassword
        val hide = binding.ivSignHide
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

    private fun clickToSignUpButton() {
        val email = binding.etSignEmail.text.toString().trim()
        val name = binding.etSignName.text.toString().trim()
        val password = binding.etSignPassword.text.toString().trim()

        signUpViewmodel.signUp(name, email, password)
    }

    private fun checkedSignUpResult() {
        signUpViewmodel.signUpResult.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                Toast.makeText(requireContext(), "회원 가입 성공!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.beginTransaction().replace(R.id.frame, MainFragment()).commit()
            } else {
                Toast.makeText(requireContext(), "회원 가입 실패!", Toast.LENGTH_SHORT).show()
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
}