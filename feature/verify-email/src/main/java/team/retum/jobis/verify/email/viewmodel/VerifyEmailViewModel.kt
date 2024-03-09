package team.retum.jobis.verify.email.viewmodel

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

internal const val EMAIL_ADDRESS = "@dsm.hs.kr"

@HiltViewModel
internal class VerifyEmailViewModel @Inject constructor(
    private val sendAuthenticationCodeUseCase: SendAuthenticationCodeUseCase,
    private val authorizeAuthenticationCodeUseCase: AuthorizeAuthenticationCodeUseCase,
) : BaseViewModel<VerifyEmailState, VerifyEmailSideEffect>(VerifyEmailState.getDefaultState()) {

    private val timerUtil: TimerUtil = TimerUtil()

    internal fun onNextClick() {
        setState { state.value.copy(buttonEnabled = false) }
        viewModelScope.launch(Dispatchers.IO) {
            val email = state.value.email + EMAIL_ADDRESS
            authorizeAuthenticationCodeUseCase(
                email = email,
                authCode = state.value.authenticationCode,
            ).onSuccess {
                postSideEffect(VerifyEmailSideEffect.MoveToVerifyPassword(email = email))
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
                        postSideEffect(VerifyEmailSideEffect.AuthenticationCodeExpiration)
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
                        postSideEffect(VerifyEmailSideEffect.MoveToVerifyPassword(email = email))
                    }
                }
            }
        }
    }

    internal fun onAuthenticationClick() {
        viewModelScope.launch(Dispatchers.IO) {
            sendAuthenticationCodeUseCase(
                email = state.value.email + EMAIL_ADDRESS,
                authCodeType = AuthCodeType.PASSWORD,
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
            }.onFailure {
                when (it) {
                    is BadRequestException -> {
                        postSideEffect(VerifyEmailSideEffect.CheckEmailValidation)
                    }

                    is ConflictException -> {
                        setState { state.value.copy(showEmailDescription = true) }
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

internal data class VerifyEmailState(
    val email: String,
    val authenticationCode: String,
    val showEmailDescription: Boolean,
    val showAuthenticationCodeDescription: Boolean,
    val emailDescriptionType: DescriptionType,
    val buttonEnabled: Boolean,
    val sendAuthenticationCode: Boolean,
    val remainTime: String,
) {
    companion object {
        fun getDefaultState() = VerifyEmailState(
            email = "",
            authenticationCode = "",
            showEmailDescription = false,
            showAuthenticationCodeDescription = false,
            emailDescriptionType = DescriptionType.Error,
            buttonEnabled = false,
            sendAuthenticationCode = false,
            remainTime = "5:00",
        )
    }
}

internal sealed interface VerifyEmailSideEffect {
    data class MoveToVerifyPassword(val email: String) : VerifyEmailSideEffect
    data object CheckEmailValidation : VerifyEmailSideEffect
    data object AuthenticationCodeExpiration : VerifyEmailSideEffect
}
