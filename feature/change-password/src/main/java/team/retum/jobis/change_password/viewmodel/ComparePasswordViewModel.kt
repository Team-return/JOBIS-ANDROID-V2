package team.retum.jobis.change_password.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import team.retum.common.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
internal class ComparePasswordViewModel @Inject constructor(

): BaseViewModel<ComparePasswordState, ComparePasswordSideEffect>(ComparePasswordState.getInitialState()) {

    internal fun setPassword(password: String) = setState {
        state.value.copy(
            password = password,
            showPasswordDescription = false,
            buttonEnabled = password.isNotBlank(),
        )
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
    data class Success(val password: String): ComparePasswordSideEffect
}
