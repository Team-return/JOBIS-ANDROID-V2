package team.retum.signup.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.AuthCodeType
import team.retum.common.exception.BadRequestException
import team.retum.common.exception.ConflictException
import team.retum.common.exception.NotFoundException
import team.retum.common.exception.UnAuthorizedException
import team.retum.common.utils.TimerUtil
import team.retum.jobisdesignsystemv2.textfield.DescriptionType
import team.retum.usecase.usecase.auth.AuthorizeAuthenticationCodeUseCase
import team.retum.usecase.usecase.auth.SendAuthenticationCodeUseCase
import javax.inject.Inject

private const val EMAIL_ADDRESS = "@dsm.hs.kr"

@HiltViewModel
internal class InputEmailViewModel @Inject constructor(
    private val sendAuthenticationCodeUseCase: SendAuthenticationCodeUseCase,
    private val authorizeAuthenticationCodeUseCase: AuthorizeAuthenticationCodeUseCase,
) : BaseViewModel<InputEmailState, InputEmailSideEffect>(InputEmailState.getDefaultState()) {

    private val timerUtil: TimerUtil = TimerUtil()
    private var authenticationClickCount: Int = 0

    internal fun onNextClick() {
        setState { state.value.copy(buttonEnabled = false) }
        viewModelScope.launch(Dispatchers.IO) {
            val email = state.value.email + EMAIL_ADDRESS
            authorizeAuthenticationCodeUseCase(
                email = email,
                authCode = state.value.authenticationCode,
            ).onSuccess {
                postSideEffect(InputEmailSideEffect.MoveToInputPassword(email = email))
            }.onFailure {
                when (it) {
                    is UnAuthorizedException -> {
                        setState {
                            state.value.copy(
                                showAuthenticationCodeDescription = true,
                                buttonEnabled = false,
                            )
                        }
                    }

                    is NotFoundException -> {
                        postSideEffect(InputEmailSideEffect.AuthenticationCodeExpiration)
                    }

                    is ConflictException -> {
                        setState {
                            state.value.copy(
                                showEmailDescription = true,
                                buttonEnabled = false,
                            )
                        }
                    }

                    is KotlinNullPointerException -> {
                        postSideEffect(InputEmailSideEffect.MoveToInputPassword(email = email))
                    }
                }
            }
        }
    }

    internal fun onAuthenticationClick() {
        viewModelScope.launch(Dispatchers.IO) {
            sendAuthenticationCodeUseCase(
                email = state.value.email + EMAIL_ADDRESS,
                authCodeType = AuthCodeType.SIGN_UP,
            ).onSuccess {
                stopAuthenticationTimer()
                startAuthenticationTimer()
                setState {
                    state.value.copy(
                        sendAuthenticationCode = true,
                        emailDescriptionType = DescriptionType.Check,
                        showEmailDescription = true,
                    )
                }
                if (authenticationClickCount > 0) {
                    setState { state.value.copy(isResend = true) }
                }
                authenticationClickCount++
            }.onFailure {
                when (it) {
                    is BadRequestException -> {
                        postSideEffect(InputEmailSideEffect.CheckEmailValidation)
                    }

                    is ConflictException -> {
                        setState {
                            state.value.copy(
                                emailDescriptionType = DescriptionType.Error,
                                showEmailDescription = true,
                            )
                        }
                    }
                }
            }
        }
    }

    internal fun setEmail(email: String) = setState {
        state.value.copy(
            email = email,
            showEmailDescription = false,
        )
    }

    internal fun onAuthenticationCodeChange(authenticationCode: String) = setState {
        state.value.copy(
            authenticationCode = authenticationCode,
            buttonEnabled = authenticationCode.isNotBlank(),
        )
    }

    private fun startAuthenticationTimer() {
        timerUtil.setTimer()
        timerUtil.startTimer()
        viewModelScope.launch {
            timerUtil.remainTime.collect {
                setState { state.value.copy(remainTime = it) }
            }
        }
    }

    private fun stopAuthenticationTimer() {
        timerUtil.stopTimer()
    }
}

internal data class InputEmailState(
    val email: String,
    val authenticationCode: String,
    val showEmailDescription: Boolean,
    val showAuthenticationCodeDescription: Boolean,
    val emailDescriptionType: DescriptionType,
    val buttonEnabled: Boolean,
    val sendAuthenticationCode: Boolean,
    val remainTime: String,
    val isResend: Boolean,
) {
    companion object {
        fun getDefaultState() = InputEmailState(
            email = "",
            authenticationCode = "",
            showEmailDescription = false,
            showAuthenticationCodeDescription = false,
            emailDescriptionType = DescriptionType.Error,
            buttonEnabled = false,
            sendAuthenticationCode = false,
            remainTime = "5:00",
            isResend = false,
        )
    }
}

internal sealed interface InputEmailSideEffect {
    data class MoveToInputPassword(val email: String) : InputEmailSideEffect
    data object CheckEmailValidation : InputEmailSideEffect
    data object AuthenticationCodeExpiration : InputEmailSideEffect
}
