package team.retum.jobis.interests.viewmodel

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.network.model.response.interests.FetchInterestsResponse
import team.retum.usecase.usecase.interests.FetchInterestsUseCase
import javax.inject.Inject

@HiltViewModel
internal class InterestsViewmodel @Inject constructor(
    private val fetchInterestsUseCase: FetchInterestsUseCase,
) : BaseViewModel<InterestsState, InterestsSideEffect>(InterestsState.getInitialState()) {
    init {
        fetchInterests()
    }

    private fun fetchInterests() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchInterestsUseCase().onSuccess {
                setState {
                    state.value.copy(
                        interestsList = it,
                    )
                }
            }
            Log.d("fetchInterests", "fetchInterests: ${state.value.interestsList}")
        }
    }
}

@Immutable
internal data class InterestsState(
    val interestsList: FetchInterestsResponse?,
) {
    companion object {
        fun getInitialState() = InterestsState(
            interestsList = null,
        )
    }
}
internal sealed class InterestsSideEffect
