import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nomorelateness.donotlate.core.domain.session.SessionManager
import com.nomorelateness.donotlate.feature.auth.domain.useCase.SignUpUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _signUpResult = MutableLiveData<Result<String>>()
    val signUpResult: LiveData<Result<String>> get() = _signUpResult

    private val _eventFlow = MutableSharedFlow<SignUpEvent>()
    val eventFlow: SharedFlow<SignUpEvent> get() = _eventFlow

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            val result = signUpUseCase(name, email, password)
            _signUpResult.value = result
            if (result.isSuccess) {
                _eventFlow.emit(SignUpEvent.SignUpSuccess)
            } else {
                _eventFlow.emit(SignUpEvent.SignUpFail)
            }
        }
    }

    // 이름 유효성 검사
    fun checkName(name: EditText, item: TextView): Boolean {
        val nameText = name.text.toString().trim()
        val namePattern = Pattern.matches("^[ㄱ-ㅣ가-힣a-zA-Z\\s]+$", nameText)
        if (namePattern) {
            item.isVisible = false
            return true
        } else {
            item.isVisible = true
            return false
        }
    }

    // 이메일 유효성 검사
    fun checkEmail(id: EditText, item: TextView): Boolean {
        val emailText = id.text.toString().trim()
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()
        if (emailPattern) {
            item.isVisible = false
            return true
        } else {
            item.isVisible = true
            return false
        }
    }

    // 비밀번호 유효성 검사 (8~20자 영문 + 숫자)
    fun checkPw(pw: EditText, item: TextView): Boolean {
        val pwText = pw.text.toString().trim()
        val pwPattern = Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9]{8,20}$", pwText)
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
        return text.isEmpty()
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
        if (nullCheck(name.text.toString()) || nullCheck(email.text.toString()) || nullCheck(
                password.text.toString()
            ) || nullCheck(
                confirmPw.text.toString()
            )
        ) {
            viewModelScope.launch {
                _eventFlow.emit(SignUpEvent.ValidationError("모든 필드를 입력하세요."))
            }
            return
        }

        if (!checkName(name, nameCheck)) {
            viewModelScope.launch {
                _eventFlow.emit(SignUpEvent.ValidationError("유효하지 않은 이름입니다."))
            }
            return
        }

        if (!checkEmail(email, emailCheck)) {
            viewModelScope.launch {
                _eventFlow.emit(SignUpEvent.ValidationError("유효하지 않은 이메일입니다."))
            }
            return
        }

        if (!checkPw(password, passwordCheck)) {
            viewModelScope.launch {
                _eventFlow.emit(SignUpEvent.ValidationError("비밀번호는 8~20자 영문과 숫자 조합이어야 합니다."))
            }
            return
        }

        if (!checkConfirmPw(password, confirmPw, confirmCheck)) {
            viewModelScope.launch {
                _eventFlow.emit(SignUpEvent.ValidationError("비밀번호가 일치하지 않습니다."))
            }
            return
        }

        signUp(name.text.toString(), email.text.toString(), password.text.toString())
    }
}

sealed class SignUpEvent {
    object SignUpSuccess : SignUpEvent()
    object SignUpFail : SignUpEvent()
    data class ValidationError(val message: String) : SignUpEvent()
}

class SignUpViewmodelFactory(
    private val signUpUseCase: SignUpUseCase,
    private val sessionManager: SessionManager
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignUpViewModel(signUpUseCase, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}