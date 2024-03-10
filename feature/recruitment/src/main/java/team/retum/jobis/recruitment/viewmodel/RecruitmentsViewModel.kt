package team.retum.jobis.recruitment.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
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
) : BaseViewModel<RecruitmentsState, Unit>(RecruitmentsState.getDefaultState()) {

    internal var recruitments: SnapshotStateList<RecruitmentsEntity.RecruitmentEntity> =
        mutableStateListOf()
        private set

    init {
        fetchTotalRecruitmentCount()
    }

    internal fun clearRecruitment() {
        recruitments.clear()
    }

    internal fun setJobCode(jobCode: Long?) = setState {
        state.value.copy(jobCode = jobCode)
    }

    internal fun setTechCode(techCode: String?) = setState {
        state.value.copy(techCode = techCode)
    }

    internal fun fetchRecruitments() {
        viewModelScope.launch(Dispatchers.IO) {
            with(state.value) {
                fetchRecruitmentsUseCase(
                    name = null,
                    page = page.toInt(),
                    jobCode = jobCode,
                    techCode = techCode,
                    winterIntern = false,
                ).onSuccess {
                    recruitments.addAll(it.recruitments)
                }
            }
        }
    }

    private fun fetchTotalRecruitmentCount() {
        viewModelScope.launch(Dispatchers.IO) {
            with(state.value) {
                fetchRecruitmentCountUseCase.invoke(
                    name = null,
                    jobCode = jobCode,
                    techCode = techCode,
                    winterIntern = false,
                ).onSuccess {
                    setState { copy(totalPage = it.totalPageCount) }
                    fetchRecruitments()
                }
            }
        }
    }

    internal fun Flow<Int?>.callNextPageByPosition() {
        viewModelScope.launch {
            val fetchNextPage = async {
                collect {
                    it?.run {
                        if (this == recruitments.lastIndex - 2) {
                            setState { state.value.copy(page = state.value.page + 1) }
                            fetchRecruitments()
                        }
                    }
                }
            }
            fetchNextPage.start()
            launch {
                state.collect {
                    if (it.page == it.totalPage) {
                        fetchNextPage.cancel()
                    }
                }
            }
        }
    }

    internal fun bookmarkRecruitment(recruitmentId: Long) {
        val index = recruitments.indexOf(recruitments.find { it.id == recruitmentId })
        val bookmarked = recruitments[index].bookmarked
        recruitments[index] = recruitments[index].copy(bookmarked = !bookmarked)

        viewModelScope.launch(Dispatchers.IO) {
            recruitmentBookmarkUseCase(recruitmentId)
        }
    }
}

internal data class RecruitmentsState(
    val totalPage: Long,
    val page: Long,
    val jobCode: Long?,
    val techCode: String?,
) {
    companion object {
        fun getDefaultState() = RecruitmentsState(
            totalPage = 0,
            page = 1,
            jobCode = null,
            techCode = null,
        )
    }
}
