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

    private val stateManager = RecruitmentStateManager

    init {
        fetchCodes()
    }

    private var _majors: MutableList<CodesEntity.CodeEntity> = mutableStateListOf()
    val majors get() = _majors

    private var _techs: SnapshotStateList<CodesEntity.CodeEntity> = mutableStateListOf()
    val techs get() = _techs

    private var _checkedSkills: SnapshotStateList<CodesEntity.CodeEntity> = mutableStateListOf()
    val checkedSkills get() = _checkedSkills

    internal fun fetchCodes() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCodeUseCase(
                keyword = state.value.keyword,
                type = state.value.type,
                parentCode = state.value.parentCode,
            ).onSuccess {
                if (state.value.type == CodeType.JOB) {
                    setType()
                    _majors.addAll(it.codes)
                } else {
                    _techs.clear()
                    _techs.addAll(it.codes)
                }
            }
        }
    }

    internal fun addSkill(
        skill: CodesEntity.CodeEntity,
        check: Boolean,
    ) {
        if (check) {
            _checkedSkills.add(skill)
        } else {
            _checkedSkills.remove(skill)
        }
    }

    internal fun setKeyword(keyword: String) = setState {
        state.value.copy(keyword = keyword)
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

object RecruitmentStateManager {
    var jobCode: Long? = null
    var techCode: String? = null
}
