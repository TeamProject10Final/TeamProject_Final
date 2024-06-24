package com.example.donotlate.core.util

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

class UtilityKeyboard {
    object UtilityKeyboard {
        fun Fragment.hideKeyboard(windowToken: IBinder) {
            requireContext().hideKeyboard(windowToken)
        }

        fun Activity.hideKeyboard() {
//            hideKeyboard(currentFocus ?: View(this))
        }

        fun Context.hideKeyboard(windowToken: IBinder) {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}