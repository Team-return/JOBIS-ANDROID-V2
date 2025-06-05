package team.retum.jobis.interests.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.CodeType
import team.retum.network.model.request.interests.InterestsToggleRequest
import team.retum.usecase.entity.CodesEntity
import team.retum.usecase.entity.interests.InterestsEntity
import team.retum.usecase.entity.interests.InterestsRecruitmentsEntity
import team.retum.usecase.usecase.code.FetchCodeUseCase
import team.retum.usecase.usecase.interests.FetchInterestsSearchRecruitmentsUseCase
import team.retum.usecase.usecase.interests.FetchInterestsUseCase
import team.retum.usecase.usecase.interests.SetInterestsToggleUseCase
import javax.inject.Inject

@HiltViewModel
internal class InterestsViewmodel @Inject constructor(
    private val fetchCodeUseCase: FetchCodeUseCase,
    private val fetchInterestsUseCase: FetchInterestsUseCase,
    private val fetchInterestsSearchRecruitmentsUseCase: FetchInterestsSearchRecruitmentsUseCase,
    private val setInterestsToggleUseCase: SetInterestsToggleUseCase,
) : BaseViewModel<InterestsState, InterestsSideEffect>(InterestsState.getInitialState()) {
    init {
        fetchInterests()
        fetchInterestsSearchRecruitments()
        fetchCodes()
    }

    private fun fetchCodes() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCodeUseCase(
                keyword = null,
                type = CodeType.JOB,
                parentCode = null,
            ).onSuccess {
                setState {
                    state.value.copy(
                        majorList = it.codes,
                    )
                }
            }
        }
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

    internal fun patchInterestsMajor() {
        viewModelScope.launch(Dispatchers.IO) {
            setInterestsToggleUseCase(
                codes = InterestsToggleRequest(codeIds = state.value.selectedMajorCodes.toMutableList()),
            ).onSuccess {
                postSideEffect(sideEffect = InterestsSideEffect.MoveToInterestsComplete(state.value.studentName))
            }.onFailure {
                postSideEffect(sideEffect = InterestsSideEffect.PatchMajorFail)
            }
        }
    }

    internal fun setMajor(majorId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentSelected = state.value.selectedMajorCodes.toMutableList()
            if (currentSelected.contains(majorId)) {
                currentSelected.remove(majorId)
            } else {
                currentSelected.add(majorId)
            }
            setState {
                state.value.copy(
                    selectedMajorCodes = currentSelected,
                    selectedMajorCount = currentSelected.size,
                )
            }
        }
    }
}

@Immutable
internal data class InterestsState(
    val studentName: String,
    val majorList: List<CodesEntity.CodeEntity>,
    val interestsMajorList: List<InterestsEntity.InterestMajorEntity>,
    val interestsRecruitments: InterestsRecruitmentsEntity?,
    val selectedMajorCodes: List<Long>,
    val selectedMajorCount: Int,
    var buttonEnable: Boolean,
) {
    companion object {
        fun getInitialState() = InterestsState(
            studentName = "",
            majorList = emptyList(),
            interestsMajorList = emptyList(),
            interestsRecruitments = null,
            selectedMajorCodes = emptyList(),
            selectedMajorCount = 0,
            buttonEnable = false,
        )
    }
}

internal sealed class InterestsSideEffect {
    data class MoveToInterestsComplete(val studentName: String) : InterestsSideEffect()
    data object PatchMajorFail : InterestsSideEffect()
}
