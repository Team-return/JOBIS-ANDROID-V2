package team.retum.jobis.interests.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.entity.interests.InterestsEntity
import team.retum.usecase.entity.interests.InterestsRecruitmentsEntity
import team.retum.usecase.usecase.interests.FetchInterestsSearchRecruitmentsUseCase
import team.retum.usecase.usecase.interests.FetchInterestsUseCase
import team.retum.usecase.usecase.interests.SetInterestsToggleUseCase
import javax.inject.Inject

@HiltViewModel
internal class InterestsViewmodel @Inject constructor(
    private val fetchInterestsUseCase: FetchInterestsUseCase,
    private val fetchInterestsSearchRecruitmentsUseCase: FetchInterestsSearchRecruitmentsUseCase,
    private val setInterestsToggleUseCase: SetInterestsToggleUseCase,
) : BaseViewModel<InterestsState, InterestsSideEffect>(InterestsState.getInitialState()) {
    init {
        fetchInterests()
        fetchInterestsSearchRecruitments()
    }

    private fun fetchInterests() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchInterestsUseCase().onSuccess {
                setState {
                    state.value.copy(
                        interestsMajorList = it,
                    )
                }
            }
        }
    }

    private fun fetchInterestsSearchRecruitments() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchInterestsSearchRecruitmentsUseCase().onSuccess {
                setState {
                    state.value.copy(
                        interestsRecruitments = it,
                    )
                }
            }
        }
    }

    internal fun setInterestsToggle(codes: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            setInterestsToggleUseCase(
                codes = codes,
            ).onSuccess {
            }
        }
    }

    internal fun setMajorId(majorId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            setState {
                state.value.copy(
                    majorId = majorId,
                )
            }
        }
    }

    internal fun updateCodeIds(codeIds: List<Int>) {
        viewModelScope.launch {
            setState {
                state.value.copy(
                    codeIds = codeIds,
                )
            }
        }
    }
}

@Immutable
internal data class InterestsState(
    val interestsMajorList: InterestsEntity?,
    val interestsRecruitments: InterestsRecruitmentsEntity?,
    val codeIds: List<Int>,
    val majorId: Int?,
) {
    companion object {
        fun getInitialState() = InterestsState(
            interestsMajorList = null,
            interestsRecruitments = null,
            codeIds = emptyList(),
            majorId = null,
        )
    }
}

internal sealed class InterestsSideEffect
