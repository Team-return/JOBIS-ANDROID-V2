package team.retum.jobis.interests.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.entity.interests.InterestsEntity
import team.retum.usecase.entity.interests.InterestsRecruitmentsEntity
import team.retum.usecase.usecase.interests.FetchInterestsSearchRecruitmentUseCase
import team.retum.usecase.usecase.interests.FetchInterestsUseCase
import team.retum.usecase.usecase.interests.SetInterestsToggleUseCase
import javax.inject.Inject

@HiltViewModel
internal class InterestsViewmodel @Inject constructor(
    private val fetchInterestsUseCase: FetchInterestsUseCase,
    private val fetchInterestsSearchRecruitmentsUseCase: FetchInterestsSearchRecruitmentUseCase,
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
                        studentName = it.studentName,
                        interestsMajorList = it.interests,
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

    internal fun setMajor(major: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setState {
                state.value.copy(
                    selectedMajor = major,
                )
            }
        }
    }

    // TODO : 단순 대입이 아닌 해당하는 전공을 해제 하거나 추가해야함
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
    val studentName: String,
    val interestsMajorList: List<InterestsEntity.InterestMajorEntity>,
    val interestsRecruitments: InterestsRecruitmentsEntity?,
    val codeIds: List<Int>,
    val selectedMajor: String,
) {
    companion object {
        fun getInitialState() = InterestsState(
            studentName = "",
            interestsMajorList = emptyList(),
            interestsRecruitments = null,
            codeIds = emptyList(),
            selectedMajor = "",
        )
    }
}

internal sealed class InterestsSideEffect
