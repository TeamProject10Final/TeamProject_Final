package com.example.donotlate.feature.auth.presentation.view

import android.graphics.Color
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.donotlate.feature.auth.domain.useCase.SignUpUseCase
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SignUpViewModel(private val signUpUseCase: SignUpUseCase) : ViewModel() {

    private val _signUpResult = MutableLiveData<Result<String>>()
    val signUpResult: LiveData<Result<String>> get() = _signUpResult

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            val result = signUpUseCase(name, email, password)
            _signUpResult.value = result
        }
    }


    //이름 유효성 검사
    fun checkName(name: EditText, item: TextView): Boolean {
        val name = name.text.toString().trim()
        val namePattern = Pattern.matches("^[ㄱ-ㅣ가-힣a-zA-Z\\s]+$", name)
        if (namePattern) {
            item.isVisible = false
            return true
        } else {
            item.isVisible = true
            return false
        }
    }

    //이메일 유효성 검사
    fun checkEmail(id: EditText, item: TextView): Boolean {
        val email = id.text.toString().trim()
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (emailPattern) {
            item.isVisible = false
            return true
        } else {
            item.isVisible = true
            return false
        }
    }

    //비밀번호 유효성 검사 (8~20자 영문 + 숫자)
    fun checkPw(pw: EditText, item: TextView): Boolean {
        val pwText = pw.text.toString().trim()
        val pwPattern = Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{8,20}$", pwText)
        if (pwPattern) {
            item.isVisible = false
            return true
        } else {
            item.isVisible = true
            return false
        }
    }

    fun checkConfirmPw(pw: EditText, confirmPw: EditText, item: TextView): Boolean {
        val pwText = pw.text.toString().trim()
        val confirmPwText = confirmPw.text.toString().trim()
        if (pwText == confirmPwText) {
            item.isVisible = false
            return true
        } else {
            item.isVisible = true
            return false
        }
    }

    fun nullCheck(text: String): Boolean {
        if (text.isEmpty()) {
            return true
        } else {
            return false
        }
    }

    fun checkSignUp(
        name: EditText,
        email: EditText,
        password: EditText,
        confirmPw: EditText,
        nameCheck: TextView,
        emailCheck: TextView,
        passwordCheck: TextView,
        confirmCheck: TextView,
        view: View
    ) {
        if (nullCheck(name.toString()) || nullCheck(email.toString()) || nullCheck(password.toString()) || nullCheck(
                confirmPw.toString()
            )
        )
        else if (!checkName(name, nameCheck))
        else if (!checkEmail(email, emailCheck))
        else if (!checkPw(password, passwordCheck))
        else if (!checkConfirmPw(password, confirmPw, confirmCheck))
        else {
            val snackbar = Snackbar.make(view, "회원가입이 완료되었습니다!", Snackbar.LENGTH_SHORT)
            snackbar.setTextColor(Color.WHITE)
            snackbar.setBackgroundTint(Color.GRAY)
            snackbar.animationMode = Snackbar.ANIMATION_MODE_FADE
            snackbar.setActionTextColor(Color.WHITE)
            snackbar.setAction("닫기") {
                snackbar.dismiss()
            }
            snackbar.show()
        }
    }
}

class SignUpViewmodelFactory(private val signUpUseCase: SignUpUseCase) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignUpViewModel(signUpUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
