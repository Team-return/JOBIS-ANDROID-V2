package team.retum.jobis.interests.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.CodeType
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

    internal fun setInterestsToggle() {
        viewModelScope.launch(Dispatchers.IO) {
            setInterestsToggleUseCase(
                codes = state.value.codeIds,
            ).onSuccess {
            }
        }
    }

    internal fun setMajor(major: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentSelected = state.value.selectedMajorCodes.toMutableList()
            if (currentSelected.contains(major)) {
                currentSelected.remove(major)
            } else {
                currentSelected.add(major)
            }
            setState {
                state.value.copy(
                    selectedMajor = major,
                    selectedMajorCodes = currentSelected,
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
    val codeIds: List<Int>,
    val selectedMajor: String,
    val selectedMajorCodes: List<String>,
) {
    companion object {
        fun getInitialState() = InterestsState(
            studentName = "",
            majorList = emptyList(),
            interestsMajorList = emptyList(),
            interestsRecruitments = null,
            codeIds = emptyList(),
            selectedMajor = "",
            selectedMajorCodes = emptyList(),
        )
    }
}

internal sealed class InterestsSideEffect
