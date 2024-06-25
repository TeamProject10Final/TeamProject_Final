package com.example.donotlate.feature.auth.presentation.view

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.core.util.UtilityKeyboard.UtilityKeyboard.hideKeyboard
import com.example.donotlate.databinding.FragmentSignupBinding
import com.example.donotlate.feature.auth.presentation.dialog.InformationDialogFragment

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

        binding.root.setOnClickListener {
            hideKeyboard(binding.root.windowToken)
//            requireActivity().currentFocus!!.clearFocus()
        }

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

        editTextProcess()


    }

    private fun editTextProcess() {
        binding.etSignConfirm.setOnEditorActionListener { textView, action, keyEvent ->
            var handled = false

            if (action == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(binding.root.windowToken)
                //requireActivity().currentFocus!!.clearFocus()
                handled = true
            }
            handled
        }
    }


    private fun startLogin() {
        binding.tvSignLogin.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .replace(R.id.frame, LoginFragment())
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
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .remove(this)
                .commit()
        }
    }

    private fun passwordHide() {
        val password = binding.etSignPassword
        val hide = binding.ivSignHide
        hide.tag = "0"
        hide.setImageResource(R.drawable.ic_hide)
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
                    hide.setImageResource(R.drawable.ic_hide)
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
                Toast.makeText(
                    requireContext(),
                    "${resources.getString(R.string.toast_login_text2)}",
                    Toast.LENGTH_SHORT
                ).show()
                parentFragmentManager.beginTransaction().replace(R.id.frame, LoginFragment())
                    .commit()
            } else {
                Toast.makeText(
                    requireContext(),
                    "${resources.getString(R.string.toast_login_text3)}",
                    Toast.LENGTH_SHORT
                ).show()
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