package team.retum.signup.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.AuthCodeType
import team.retum.jobisdesignsystemv2.textfield.DescriptionType
import team.retum.usecase.usecase.auth.SendAuthenticationCodeUseCase
import javax.inject.Inject

private const val EmailAddress = "@dsm.hs.kr"

@HiltViewModel
internal class InputEmailViewModel @Inject constructor(
    private val sendAuthenticationCodeUseCase: SendAuthenticationCodeUseCase,
) : BaseViewModel<InputEmailState, InputEmailSideEffect>(InputEmailState.getDefaultState()) {

    internal fun onNextClick() {

    }

    internal fun onAuthenticationClick() {
        viewModelScope.launch(Dispatchers.IO) {
            sendAuthenticationCodeUseCase(
                email = state.value.email + EmailAddress,
                authCodeType = AuthCodeType.SIGN_UP,
            ).onSuccess {
                setState {
                    state.value.copy(
                        sendAuthenticationCode = true,
                        emailDescriptionType = DescriptionType.Check,
                        showEmailDescription = true,
                    )
                }
            }.onFailure {

            }
        }

    }


    internal fun onEmailChange(email: String) = setState {
        state.value.copy(email = email)
    }

    internal fun onAuthenticationCodeChange(authenticationCode: String) = setState {
        state.value.copy(authenticationCode = authenticationCode)
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
        )
    }
}

internal sealed interface InputEmailSideEffect {

}
