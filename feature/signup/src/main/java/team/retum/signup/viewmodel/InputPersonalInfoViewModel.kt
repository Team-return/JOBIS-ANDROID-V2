package team.retum.signup.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.usecase.student.CheckStudentExistsUseCase
import javax.inject.Inject

internal const val MAX_LENGTH_NUMBER = 4

@HiltViewModel
internal class InputPersonalInfoViewModel @Inject constructor(
    private val checkStudentExistsUseCase: CheckStudentExistsUseCase,
) : BaseViewModel<InputPersonalInfoState, InputPersonalInfoSideEffect>(
    initialState = InputPersonalInfoState.getDefaultState(),
) {
    internal fun setName(name: String) {
        setState {
            state.value.copy(
                name = name,
                showNameDescription = name.isBlank(),
            )
        }
        setButtonEnabled()
    }

    internal fun setNumber(number: String) {
        setState {
            state.value.copy(
                number = number,
                showNumberDescription = false,
            )
        }
        setButtonEnabled()
    }

    private fun setButtonEnabled() = setState {
        state.value.copy(buttonEnabled = getButtonEnabled())
    }

    private fun getButtonEnabled() = state.value.run {
        val hasNoError = !showNameDescription && !showNumberDescription
        val hasNoBlank = name.isNotBlank() && number.length == MAX_LENGTH_NUMBER
        hasNoError && hasNoBlank
    }

    internal fun onNextClick() {
        with(state.value) {
            setState { copy(buttonEnabled = false) }
            viewModelScope.launch(Dispatchers.IO) {
                checkStudentExistsUseCase(
                    gcn = number,
                    name = name,
                ).onSuccess {
                    val grade = number[0].toString()
                    val classRoom = number[1].toString()
                    val number = number.substring(2..3)
                    postSideEffect(
                        sideEffect = InputPersonalInfoSideEffect.MoveToNext(
                            name = name.trim(),
                            grade = grade,
                            classRoom = classRoom,
                            number = number,
                        )
                    )
                }.onFailure {
                    setState {
                        copy(
                            showNumberDescription = true,
                            buttonEnabled = false,
                        )
                    }
                }
            }
        }
    }
}

internal data class InputPersonalInfoState(
    val name: String,
    val number: String,
    val buttonEnabled: Boolean,
    val showNameDescription: Boolean,
    val showNumberDescription: Boolean,
) {
    companion object {
        fun getDefaultState() = InputPersonalInfoState(
            name = "",
            number = "",
            buttonEnabled = false,
            showNameDescription = false,
            showNumberDescription = false,
        )
    }
}

internal sealed interface InputPersonalInfoSideEffect {
    data class MoveToNext(
        val name: String,
        val grade: String,
        val classRoom: String,
        val number: String,
    ) : InputPersonalInfoSideEffect
}
