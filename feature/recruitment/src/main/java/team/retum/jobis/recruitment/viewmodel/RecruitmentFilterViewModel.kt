package team.retum.jobis.recruitment.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.CodeType
import team.retum.usecase.entity.CodesEntity
import team.retum.usecase.usecase.code.FetchCodeUseCase
import javax.inject.Inject

@HiltViewModel
internal class RecruitmentFilterViewModel @Inject constructor(
    private val fetchCodeUseCase: FetchCodeUseCase,
) : BaseViewModel<RecruitmentFilterState, RecruitmentFilterSideEffect>(RecruitmentFilterState.getDefaultState()) {

    companion object {
        var jobCode: Long? = null
        var techCode: String? = null
    }
    init {
        fetchCodes()
    }

    var majors: SnapshotStateList<CodesEntity.CodeEntity> = mutableStateListOf()
        private set
    var techs: SnapshotStateList<CodesEntity.CodeEntity> = mutableStateListOf()
        private set
    var checkedSkills: SnapshotStateList<CodesEntity.CodeEntity> = mutableStateListOf()
        private set

    internal fun fetchCodes() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCodeUseCase(
                keyword = state.value.keyword,
                type = state.value.type,
                parentCode = state.value.parentCode,
            ).onSuccess {
                if (state.value.type == CodeType.JOB) {
                    setType()
                    majors.addAll(it.codes)
                } else {
                    techs.clear()
                    techs.addAll(it.codes)
                }
            }
        }
    }

    internal fun addSkill(
        skill: CodesEntity.CodeEntity,
        check: Boolean,
    ) {
        if (check) {
            checkedSkills.add(skill)
        } else {
            checkedSkills.remove(skill)
        }
    }

    internal fun setKeyword(keyword: String) {
        setState { state.value.copy(keyword = keyword) }
    }

    internal fun setSelectedMajor(
        major: String,
        parentCode: Long?,
    ) {
        setState {
            state.value.copy(
                selectedMajor = major,
                parentCode = parentCode,
            )
        }
    }

    private fun setType() =
        setState { state.value.copy(type = CodeType.TECH) }
}

internal data class RecruitmentFilterState(
    val type: CodeType,
    val keyword: String?,
    val selectedMajor: String,
    val parentCode: Long?,
) {
    companion object {
        fun getDefaultState() = RecruitmentFilterState(
            type = CodeType.JOB,
            keyword = null,
            selectedMajor = "",
            parentCode = null,
        )
    }
}

internal sealed interface RecruitmentFilterSideEffect {
    data object BadRequest : RecruitmentFilterSideEffect
}
