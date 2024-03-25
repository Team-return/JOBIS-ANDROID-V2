package team.retum.signup.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.exception.OfflineException
import team.retum.signup.model.SignUpData
import team.retum.usecase.usecase.student.PostSignUpUseCase
import team.retum.usecase.usecase.user.GetDeviceTokenUseCase
import javax.inject.Inject

@HiltViewModel
internal class TermsViewModel @Inject constructor(
    private val postSignUpUseCase: PostSignUpUseCase,
    private val getDeviceTokenUseCase: GetDeviceTokenUseCase,
) : BaseViewModel<TermsState, TermsSideEffect>(TermsState.getInitialState()) {

    private lateinit var deviceToken: String

    init {
        getDeviceToken()
    }

    private fun getDeviceToken() {
        viewModelScope.launch(Dispatchers.IO) {
            getDeviceTokenUseCase().onSuccess {
                deviceToken = it
            }
        }
    }

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
                    deviceToken = deviceToken,
                ).onSuccess {
                    postSideEffect(TermsSideEffect.Success)
                }.onFailure {
                    when (it) {
                        is OfflineException -> {
                            postSideEffect(TermsSideEffect.CheckInternetConnection)
                        }

                        else -> {
                            postSideEffect(TermsSideEffect.UnknownException)
                        }
                    }
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
    data object CheckInternetConnection : TermsSideEffect
    data object UnknownException : TermsSideEffect
}
