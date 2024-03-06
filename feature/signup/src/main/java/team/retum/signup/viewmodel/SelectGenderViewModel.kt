package team.retum.signup.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.Gender
import javax.inject.Inject

@HiltViewModel
internal class SelectGenderViewModel @Inject constructor() :
    BaseViewModel<SelectGenderState, SelectGenderSideEffect>(SelectGenderState.getDefaultState()) {
    internal fun setGender(gender: Gender) = setState {
        state.value.copy(
            gender = gender,
            buttonEnabled = true,
        )
    }

    internal fun onNextClick() {
        state.value.gender?.let {
            postSideEffect(SelectGenderSideEffect.MoveToNext(gender = it))
        }
    }
}

internal data class SelectGenderState(
    val gender: Gender?,
    val buttonEnabled: Boolean,
) {
    companion object {
        fun getDefaultState() = SelectGenderState(
            gender = null,
            buttonEnabled = false,
        )
    }
}

internal sealed interface SelectGenderSideEffect {
    data class MoveToNext(val gender: Gender) : SelectGenderSideEffect
}
