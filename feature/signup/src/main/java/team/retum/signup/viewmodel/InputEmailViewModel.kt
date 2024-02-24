package team.retum.signup.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import team.retum.common.base.BaseViewModel
import team.retum.jobisdesignsystemv2.textfield.DescriptionType
import javax.inject.Inject

@HiltViewModel
internal class InputEmailViewModel @Inject constructor(

) : BaseViewModel<InputEmailState, InputEmailSideEffect>(InputEmailState.getDefaultState()) {

    internal fun onNextClick() {

    }

    internal fun onAuthenticationClick() {

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
) {
    companion object {
        fun getDefaultState() = InputEmailState(
            email = "",
            authenticationCode = "",
            showEmailDescription = false,
            showAuthenticationCodeDescription = false,
            emailDescriptionType = DescriptionType.Error,
            buttonEnabled = false,
        )
    }
}

internal sealed interface InputEmailSideEffect {

}
