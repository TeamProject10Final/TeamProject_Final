package com.example.donotlate.feature.auth.presentation.view

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.MainActivity
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentSignupBinding
import com.example.donotlate.feature.auth.presentation.dialog.InformationDialogFragment
import com.example.donotlate.feature.auth.presentation.validation.CheckValidation
import com.example.donotlate.feature.auth.presentation.viewmodel.SignUpViewModel
import com.example.donotlate.feature.auth.presentation.viewmodel.SignUpViewmodelFactory
import com.example.donotlate.feature.main.presentation.view.MainFragment
import com.google.android.material.snackbar.Snackbar

class SignupFragment : Fragment(), CheckValidation {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val signUpViewmodel: SignUpViewModel by viewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        SignUpViewmodelFactory(appContainer.signUpUseCase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
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


        name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                checkName(name, nameCheck)
            }

            override fun afterTextChanged(s: Editable?) {
                checkName(name, nameCheck)
            }

        })

        email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                checkEmail(email, emailCheck)
            }

            override fun afterTextChanged(s: Editable?) {
                checkEmail(email, emailCheck)
            }
        })

        password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                checkPw(password, passwordCheck)
            }

            override fun afterTextChanged(s: Editable?) {
                checkPw(password, passwordCheck)
            }
        })

        confirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                checkConfirmPw(password, confirm, confirmCheck)
            }

            override fun afterTextChanged(s: Editable?) {
                checkConfirmPw(password, confirm, confirmCheck)
            }

        })

        binding.btnSignUp.setOnClickListener {
            if (nullCheck(name.toString()) || nullCheck(email.toString()) || nullCheck(password.toString()) || nullCheck(
                    confirm.toString()
                )
            )
            else if (!checkName(name, nameCheck))
            else if (!checkEmail(email, emailCheck))
            else if (!checkPw(password, passwordCheck))
            else if (!checkConfirmPw(password, confirm, confirmCheck))
            else {
                val snackbar = Snackbar.make(it, "회원가입이 완료되었습니다!", Snackbar.LENGTH_SHORT)
                snackbar.setTextColor(Color.WHITE)
                snackbar.setBackgroundTint(Color.GRAY)
                snackbar.animationMode = Snackbar.ANIMATION_MODE_FADE
                snackbar.setActionTextColor(Color.WHITE)
                snackbar.setAction("닫기") {
                    snackbar.dismiss()
                }
                snackbar.show()
                val activity = activity as MainActivity
                activity.removeFragment(this)
            }
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

    private fun clickToSignUpButton(){
        val email = binding.etSignEmail.text.toString().trim()
        val name = binding.etSignName.text.toString().trim()
        val password = binding.etSignPassword.text.toString().trim()

        signUpViewmodel.signUp(name,email,password)
    }

    private fun checkedSignUpResult(){
        signUpViewmodel.signUpResult.observe(viewLifecycleOwner){result ->
            if(result.isSuccess)  {
                Toast.makeText(requireContext(), "회원 가입 성공!", Toast.LENGTH_SHORT).show()
                val activity = activity as MainActivity
                activity.changeFragment(MainFragment())
            }else{
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