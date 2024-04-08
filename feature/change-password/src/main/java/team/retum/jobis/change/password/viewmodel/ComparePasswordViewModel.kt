package team.retum.jobis.change.password.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.exception.BadRequestException
import team.retum.usecase.usecase.student.ComparePasswordUseCase
import javax.inject.Inject

@HiltViewModel
internal class ComparePasswordViewModel @Inject constructor(
    private val comparePasswordUseCase: ComparePasswordUseCase,
) : BaseViewModel<ComparePasswordState, ComparePasswordSideEffect>(ComparePasswordState.getInitialState()) {

    internal fun setPassword(password: String) = setState {
        state.value.copy(
            password = password,
            showPasswordDescription = false,
            buttonEnabled = password.isNotBlank(),
        )
    }

    internal fun comparePassword() {
        viewModelScope.launch(Dispatchers.IO) {
            val password = state.value.password
            comparePasswordUseCase(password = password).onSuccess {
                postSideEffect(ComparePasswordSideEffect.Success(password = password))
            }.onFailure {
                setState {
                    state.value.copy(showPasswordDescription = it is BadRequestException)
                }
            }
        }
    }
}

internal data class ComparePasswordState(
    val password: String,
    val showPasswordDescription: Boolean,
    val buttonEnabled: Boolean,
) {
    companion object {
        fun getInitialState() = ComparePasswordState(
            password = "",
            showPasswordDescription = false,
            buttonEnabled = false,
        )
    }
}

internal sealed interface ComparePasswordSideEffect {
    data class Success(val password: String) : ComparePasswordSideEffect
}
