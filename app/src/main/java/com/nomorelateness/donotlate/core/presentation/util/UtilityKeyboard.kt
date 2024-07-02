package com.nomorelateness.donotlate.core.presentation.util

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

// FIXME("최상위 함수로 만들 것")
class UtilityKeyboard {
    object UtilityKeyboard {
        fun Fragment.hideKeyboard(windowToken: IBinder) {
            requireContext().hideKeyboard(windowToken)
        }

        fun Context.hideKeyboard(windowToken: IBinder) {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}
