package team.retum.jobis.recruitment.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.exception.BadRequestException
import team.retum.usecase.entity.RecruitmentsEntity
import team.retum.usecase.usecase.bookmark.BookmarkRecruitmentUseCase
import team.retum.usecase.usecase.recruitment.FetchRecruitmentCountUseCase
import team.retum.usecase.usecase.recruitment.FetchRecruitmentsUseCase
import javax.inject.Inject

@HiltViewModel
internal class RecruitmentViewModel @Inject constructor(
    private val fetchRecruitmentsUseCase: FetchRecruitmentsUseCase,
    private val fetchRecruitmentCountUseCase: FetchRecruitmentCountUseCase,
    private val recruitmentBookmarkUseCase: BookmarkRecruitmentUseCase,
) : BaseViewModel<RecruitmentState, RecruitmentSideEffect>(RecruitmentState.getDefaultState()) {

    private var _recruitments: MutableState<RecruitmentsEntity> =
        mutableStateOf(RecruitmentsEntity(emptyList()))
    val recruitments: MutableState<RecruitmentsEntity> get() = _recruitments

    internal fun setName(name: String) {
        _recruitments = mutableStateOf(RecruitmentsEntity(emptyList()))
        setState { state.value.copy(name = name, page = 1) }
    }

    internal fun setCheckRecruitment(check: Boolean) =
        setState { state.value.copy(checkRecruitment = check) }

    internal fun fetchRecruitments(
        page: Int,
        name: String?,
        jobCode: Long?,
        techCode: String?,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchRecruitmentsUseCase.invoke(
                page = page,
                name = name,
                jobCode = jobCode,
                techCode = techCode,
                winterIntern = false,
            ).onSuccess {
                val currentRecruitments = _recruitments.value.recruitments.toMutableList()
                currentRecruitments.addAll(it.recruitments)
                val newRecruitmentsEntity =
                    _recruitments.value.copy(recruitments = currentRecruitments)
                _recruitments.value = newRecruitmentsEntity
                setState { state.value.copy(page = page + 1) }
            }.onFailure {
                when (it) {
                    is BadRequestException -> {
                        postSideEffect(RecruitmentSideEffect.BadRequest)
                    }
                }
            }
        }
    }

    internal fun fetchTotalRecruitmentCount(
        name: String?,
        jobCode: Long?,
        techCode: String?,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchRecruitmentCountUseCase.invoke(
                name = name,
                jobCode = jobCode,
                techCode = techCode,
                winterIntern = false,
            ).onSuccess {
                setState { state.value.copy(totalPage = it.totalPageCount) }
            }
        }
    }

    internal fun bookmarkRecruitment(recruitmentId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            recruitmentBookmarkUseCase(recruitmentId).onFailure {
                when (it) {
                    is BadRequestException -> {
                        postSideEffect(RecruitmentSideEffect.BadRequest)
                    }
                }
            }
            fetchRecruitments(
                page = state.value.page,
                name = state.value.name,
                jobCode = null,
                techCode = null,
            )
        }
    }
}

internal data class RecruitmentState(
    val name: String?,
    val totalPage: Long,
    val checkRecruitment: Boolean,
    val page: Int,
) {
    companion object {
        fun getDefaultState() = RecruitmentState(
            name = null,
            totalPage = 0,
            checkRecruitment = true,
            page = 1,
        )
    }
}

internal sealed interface RecruitmentSideEffect {
    data object BadRequest : RecruitmentSideEffect
}
