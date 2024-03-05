package team.retum.jobis.recruitment.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.CodeType
import team.retum.usecase.entity.CodesEntity
import team.retum.usecase.usecase.code.FetchCodeUseCase
import javax.inject.Inject

internal class RecruitmentFilterViewModel @Inject constructor(
    private val fetchCodeUseCase: FetchCodeUseCase,
) : BaseViewModel<RecruitmentFilterState, RecruitmentFilterSideEffect>(RecruitmentFilterState.getDefaultState()) {
    private val _techs = mutableListOf<CodesEntity.CodeEntity>()
    val techs: MutableList<CodesEntity.CodeEntity> get() = _techs

    internal fun fetchCodes() {
        val type = state.value.type
        viewModelScope.launch(Dispatchers.IO) {
            fetchCodeUseCase(
                keyword = state.value.keyword,
                type = type,
                parentCode = state.value.selectedJobCode,
            ).onSuccess {
                when (type) {
                    CodeType.JOB -> setJobs(jobs = it.codes)

                    CodeType.TECH -> {
                        with(_techs) {
                            clear()
                            addAll(it.codes)
                            setTechs(techs = this)
                        }
                    }

                    CodeType.BUSINESS_AREA -> setBusinessAreas(businessAreas = it.codes)
                }
            }
        }
    }

    private fun setJobs(jobs: List<CodesEntity.CodeEntity>) =
        setState { state.value.copy(jobs = jobs.sortedByDescending { it.keyword.length }) }

    private fun setTechs(techs: MutableList<CodesEntity.CodeEntity>) =
        setState { state.value.copy(techs = techs.toMutableStateList()) }

    private fun setBusinessAreas(businessAreas: List<CodesEntity.CodeEntity>) =
        setState { state.value.copy(businessAreas = businessAreas) }

    internal fun setKeyword(keyword: String) {
        setState { state.value.copy(keyword = keyword) }
        searchTechCode(keyword)
    }

    internal fun setType(type: CodeType) =
        setState { state.value.copy(type = type) }

    internal fun setParentCode(parentCode: Long?) =
        setState { state.value.copy(selectedJobCode = parentCode) }
    
    internal fun onSelectTech(
        code: Long,
        keyword: String,
    ) {
        val tech = code to keyword

        with(state.value.selectedTechs) {
            if (contains(tech)) {
                remove(tech)
            } else {
                add(tech)
            }
        }
        setState {
            state.value.copy(selectedTechs = state.value.selectedTechs)
        }
    }

    private fun searchTechCode(
        keyword: String,
    ) {
        val resultList = mutableListOf<CodesEntity.CodeEntity>()

        _techs.filter {
            keyword.length <= it.keyword.length && keyword.uppercase() == it.keyword.substring(
                keyword.indices,
            ).uppercase()
        }.map {
            resultList.add(it)
        }

        setTechs(
            techs = if (keyword.isBlank()) {
                _techs
            } else {
                resultList
            },
        )
    }
}

internal data class RecruitmentFilterState(
    val jobs: List<CodesEntity.CodeEntity>,
    val techs: SnapshotStateList<CodesEntity.CodeEntity>,
    val businessAreas: List<CodesEntity.CodeEntity>,
    val selectedTechs: SnapshotStateList<Pair<Long, String>>,
    val type: CodeType,
    val selectedJobCode: Long?,
    val keyword: String?,
) {
    companion object {
        fun getDefaultState() = RecruitmentFilterState(
            jobs = emptyList(),
            techs = mutableStateListOf(),
            businessAreas = emptyList(),
            selectedTechs = mutableStateListOf(),
            type = CodeType.JOB,
            selectedJobCode = null,
            keyword = null,
        )
    }
}

internal sealed interface RecruitmentFilterSideEffect {
    data object BadRequest : RecruitmentFilterSideEffect
}
