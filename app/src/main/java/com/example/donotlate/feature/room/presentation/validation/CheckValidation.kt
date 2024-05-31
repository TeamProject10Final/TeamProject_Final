package com.example.donotlate.feature.room.presentation.validation

import android.graphics.Color
import android.widget.EditText
import android.widget.TextView

interface CheckValidation {

    //이메일 유효성 검사
    fun checkEmail(id: EditText, item: TextView): Boolean {
        val email = id.text.toString().trim()
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (emailPattern) {
            item.setTextColor(Color.parseColor("#000000"))
            return true
        } else {
            item.setTextColor(Color.parseColor("#D32222"))
            return false
        }
    }
}