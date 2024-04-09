package team.retum.signup.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import team.retum.common.base.BaseViewModel
import team.retum.common.utils.Regex
import team.retum.jobisdesignsystemv2.textfield.DescriptionType
import java.net.URLEncoder
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
internal class SetPasswordViewModel @Inject constructor() :
    BaseViewModel<SetPasswordState, SetPasswordSideEffect>(
        initialState = SetPasswordState.getDefaultState(),
    ) {
    internal fun setPassword(password: String) {
        setState {
            with(state.value) {
                copy(
                    password = password,
                    showPasswordDescription = !Pattern.matches(
                        Regex.PASSWORD,
                        password,
                    ),
                    passwordDescriptionType = if (password.isBlank()) {
                        DescriptionType.Information
                    } else {
                        DescriptionType.Error
                    },
                )
            }
        }
        setButtonEnabled()
    }

    internal fun setRepeatPassword(repeatPassword: String) {
        setState {
            with(state.value) {
                copy(
                    repeatPassword = repeatPassword,
                    showRepeatPasswordDescription = password != repeatPassword,
                )
            }
        }
        setButtonEnabled()
    }

    private fun setButtonEnabled() = setState {
        with(state.value) {
            val hasBlank = password.isBlank() || repeatPassword.isBlank()
            val hasInvalidation = showPasswordDescription || showRepeatPasswordDescription
            copy(buttonEnabled = !hasBlank && !hasInvalidation)
        }
    }

    internal fun onNextClick() {
        setState { state.value.copy(buttonEnabled = false) }
        val encodedPassword = URLEncoder.encode(state.value.password, "UTF8")
        postSideEffect(SetPasswordSideEffect.MoveToNext(password = encodedPassword))
    }
}

internal data class SetPasswordState(
    val password: String,
    val repeatPassword: String,
    val showPasswordDescription: Boolean,
    val passwordDescriptionType: DescriptionType,
    val showRepeatPasswordDescription: Boolean,
    val buttonEnabled: Boolean,
) {
    companion object {
        fun getDefaultState() = SetPasswordState(
            password = "",
            repeatPassword = "",
            showPasswordDescription = true,
            passwordDescriptionType = DescriptionType.Information,
            showRepeatPasswordDescription = false,
            buttonEnabled = false,
        )
    }
}

internal sealed interface SetPasswordSideEffect {
    data class MoveToNext(val password: String) : SetPasswordSideEffect
}
