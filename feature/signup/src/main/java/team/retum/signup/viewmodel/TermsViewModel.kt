package team.retum.signup.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.signup.model.SignUpData
import team.retum.usecase.usecase.student.PostSignUpUseCase
import javax.inject.Inject

@HiltViewModel
internal class TermsViewModel @Inject constructor(
    private val postSignUpUseCase: PostSignUpUseCase,
) : BaseViewModel<TermsState, TermsSideEffect>(TermsState.getInitialState()) {

    internal fun onReachTheEnded(buttonEnabled: Boolean) = setState {
        state.value.copy(buttonEnabled = buttonEnabled)
    }

    internal fun onCompleteClick(signUpData: SignUpData) {
        with(signUpData) {
            viewModelScope.launch(Dispatchers.IO) {
                postSignUpUseCase(
                    email = email,
                    password = password,
                    grade = grade,
                    name = name,
                    gender = gender!!,
                    classRoom = classRoom.toLong(),
                    number = number.toLong(),
                    profileImageUrl = profileImageUrl.replace(" ", "/"),
                ).onSuccess {
                    postSideEffect(TermsSideEffect.Success)
                }
            }
        }
    }
}

internal data class TermsState(
    val buttonEnabled: Boolean,
) {
    companion object {
        fun getInitialState() = TermsState(
            buttonEnabled = false,
        )
    }
}

internal sealed interface TermsSideEffect {
    data object Success : TermsSideEffect
}
