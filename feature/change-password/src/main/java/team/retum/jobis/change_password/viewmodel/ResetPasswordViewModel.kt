package team.retum.jobis.change_password.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.exception.NotFoundException
import team.retum.common.utils.Regex
import team.retum.jobisdesignsystemv2.textfield.DescriptionType
import team.retum.usecase.usecase.student.ChangePasswordUseCase
import team.retum.usecase.usecase.student.ResetPasswordUseCase
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
internal class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
) : BaseViewModel<ResetPasswordState, ResetPasswordSideEffect>(ResetPasswordState.getInitialState()) {

    internal fun setEmail(email: String?) = setState {
        state.value.copy(email = email)
    }

    internal fun setCurrentPassword(currentPassword: String?) = setState {
        state.value.copy(currentPassword = currentPassword)
    }

    internal fun setPassword(currentPassword: String) {
        setState {
            with(state.value) {
                copy(
                    password = currentPassword,
                    showPasswordDescription = !Pattern.matches(
                        Regex.PASSWORD,
                        currentPassword,
                    ),
                    passwordDescriptionType = if (currentPassword.isBlank()) {
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

    internal fun onCompleteClick() {
        with(state.value) {
            viewModelScope.launch(Dispatchers.IO) {
                if (currentPassword != null) {
                    changePassword(currentPassword = currentPassword)
                } else if (email != null) {
                    resetPassword(email = email)
                }
            }
        }
    }

    private suspend fun resetPassword(email: String) = with(state.value) {
        resetPasswordUseCase(
            email = email,
            password = password,
        ).onSuccess {
            postSideEffect(ResetPasswordSideEffect.Success)
        }.onFailure {
            postSideEffect(ResetPasswordSideEffect.ClearFocus)
            when (it) {
                is NotFoundException -> {
                    postSideEffect(ResetPasswordSideEffect.NotFoundEmail)
                }

                is NullPointerException -> {
                    postSideEffect(ResetPasswordSideEffect.Success)
                }
            }
        }
    }

    private suspend fun changePassword(currentPassword: String) = with(state.value) {
        changePasswordUseCase(
            currentPassword = currentPassword,
            newPassword = password,
        ).onSuccess {
            postSideEffect(ResetPasswordSideEffect.Success)
        }.onFailure {
            postSideEffect(ResetPasswordSideEffect.ClearFocus)
            when (it) {
                is NullPointerException -> {
                    postSideEffect(ResetPasswordSideEffect.Success)
                }
            }
        }
    }
}

internal data class ResetPasswordState(
    val email: String?,
    val currentPassword: String?,
    val password: String,
    val repeatPassword: String,
    val passwordDescriptionType: DescriptionType,
    val showPasswordDescription: Boolean,
    val showRepeatPasswordDescription: Boolean,
    val buttonEnabled: Boolean,
) {
    companion object {
        fun getInitialState() = ResetPasswordState(
            email = null,
            currentPassword = null,
            password = "",
            repeatPassword = "",
            passwordDescriptionType = DescriptionType.Information,
            showPasswordDescription = true,
            showRepeatPasswordDescription = false,
            buttonEnabled = false,
        )
    }
}

internal sealed interface ResetPasswordSideEffect {
    data object Success : ResetPasswordSideEffect
    data object ClearFocus : ResetPasswordSideEffect
    data object NotFoundEmail : ResetPasswordSideEffect
}
