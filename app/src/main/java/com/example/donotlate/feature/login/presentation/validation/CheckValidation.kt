package com.example.donotlate.feature.login.presentation.validation

import android.graphics.Color
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import java.util.regex.Pattern

interface CheckValidation {

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
}