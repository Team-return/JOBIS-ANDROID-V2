package team.retum.signin.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.exception.BadRequestException
import team.retum.common.exception.ConnectionTimeOutException
import team.retum.common.exception.NotFoundException
import team.retum.common.exception.OfflineException
import team.retum.common.exception.UnAuthorizedException
import team.retum.usecase.usecase.user.GetDeviceTokenUseCase
import team.retum.usecase.usecase.user.SignInUseCase
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val getDeviceTokenUseCase: GetDeviceTokenUseCase,
) : BaseViewModel<SignInState, SignInSideEffect>(SignInState.getDefaultState()) {

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

    internal fun setEmail(email: String) {
        setState {
            state.value.copy(
                email = email,
                showEmailDescription = false,
            )
        }
        setButtonEnabled()
    }

    internal fun setPassword(password: String) {
        setState {
            state.value.copy(
                password = password,
                showPasswordDescription = false,
            )
        }
        setButtonEnabled()
    }

    private fun setButtonEnabled() = setState {
        with(state.value) {
            val isSignInValueNotBlank = email.isNotBlank() && password.isNotBlank()
            val hasNoError = !showEmailDescription && !showPasswordDescription
            copy(buttonEnabled = isSignInValueNotBlank && hasNoError)
        }
    }

    internal fun signIn() {
        setState { state.value.copy(buttonEnabled = false) }
        viewModelScope.launch(Dispatchers.IO) {
            signInUseCase(
                email = state.value.email,
                password = state.value.password,
                deviceToken = deviceToken,
            ).onSuccess {
                postSideEffect(SignInSideEffect.Success)
            }.onFailure {
                when (it) {
                    is BadRequestException -> {
                        postSideEffect(SignInSideEffect.BadRequest)
                    }

                    is OfflineException -> {
                        postSideEffect(SignInSideEffect.CheckInternetConnection)
                    }

                    is ConnectionTimeOutException -> {
                        postSideEffect(SignInSideEffect.ServerTimeOut)
                    }

                    else -> {
                        setState {
                            state.value.copy(
                                showEmailDescription = it is NotFoundException,
                                showPasswordDescription = it is UnAuthorizedException,
                            )
                        }
                    }
                }
            }
        }
    }
}

data class SignInState(
    val email: String,
    val password: String,
    val buttonEnabled: Boolean,
    val showEmailDescription: Boolean,
    val showPasswordDescription: Boolean,
) {
    companion object {
        fun getDefaultState() = SignInState(
            email = "",
            password = "",
            buttonEnabled = false,
            showEmailDescription = false,
            showPasswordDescription = false,
        )
    }
}

sealed interface SignInSideEffect {
    data object BadRequest : SignInSideEffect
    data object Success : SignInSideEffect
    data object CheckInternetConnection : SignInSideEffect
    data object ServerTimeOut : SignInSideEffect
}
