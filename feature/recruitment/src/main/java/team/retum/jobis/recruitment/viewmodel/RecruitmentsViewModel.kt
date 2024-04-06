package team.retum.jobis.recruitment.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.entity.RecruitmentsEntity
import team.retum.usecase.usecase.bookmark.BookmarkRecruitmentUseCase
import team.retum.usecase.usecase.recruitment.FetchRecruitmentCountUseCase
import team.retum.usecase.usecase.recruitment.FetchRecruitmentsUseCase
import javax.inject.Inject

private const val NUMBER_OF_ITEM_ON_PAGE = 12
private const val LAST_INDEX_OF_PAGE = 11

@HiltViewModel
internal class RecruitmentViewModel @Inject constructor(
    private val fetchRecruitmentsUseCase: FetchRecruitmentsUseCase,
    private val fetchRecruitmentCountUseCase: FetchRecruitmentCountUseCase,
    private val recruitmentBookmarkUseCase: BookmarkRecruitmentUseCase,
) : BaseViewModel<RecruitmentsState, Unit>(RecruitmentsState.getDefaultState()) {

    private val _recruitments: SnapshotStateList<RecruitmentsEntity.RecruitmentEntity> =
        mutableStateListOf()
    val recruitments: List<RecruitmentsEntity.RecruitmentEntity> = _recruitments

    internal fun clearRecruitment() {
        if (state.value.jobCode != null || state.value.techCode != null) {
            _recruitments.clear()
        }
    }

    internal fun setJobCode(jobCode: Long?) = setState {
        state.value.copy(jobCode = jobCode)
    }

    internal fun setTechCode(techCode: String?) = setState {
        state.value.copy(techCode = techCode)
    }

    internal fun fetchRecruitments() {
        addRecruitmentEntities()
        addPage()
        viewModelScope.launch(Dispatchers.IO) {
            with(state.value) {
                fetchRecruitmentsUseCase(
                    name = null,
                    page = page.toInt(),
                    jobCode = jobCode,
                    techCode = techCode,
                    winterIntern = false,
                ).onSuccess {
                    replaceRecruitments(it.recruitments)
                }
            }
        }
    }

    private fun addPage() = setState {
        state.value.copy(page = state.value.page + 1)
    }

    private fun addRecruitmentEntities() {
        repeat(NUMBER_OF_ITEM_ON_PAGE) {
            _recruitments.add(RecruitmentsEntity.RecruitmentEntity.getDefaultEntity())
        }
    }

    private fun replaceRecruitments(recruitments: List<RecruitmentsEntity.RecruitmentEntity>) {
        val startIndex = _recruitments.lastIndex - LAST_INDEX_OF_PAGE
        recruitments.forEachIndexed { index, recruitmentEntity ->
            _recruitments[startIndex + index] = recruitmentEntity
        }
        _recruitments.removeAll(_recruitments.filter { item -> item.id == 0L })
    }

    internal fun fetchTotalRecruitmentCount() {
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

    internal fun whetherFetchNextPage(lastVisibleItemIndex: Int): Boolean = with(state.value) {
        return lastVisibleItemIndex == _recruitments.lastIndex && page < totalPage
    }

    internal fun bookmarkRecruitment(recruitmentId: Long) {
        val index = _recruitments.indexOf(_recruitments.find { it.id == recruitmentId })
        val bookmarked = _recruitments[index].bookmarked
        _recruitments[index] = _recruitments[index].copy(bookmarked = !bookmarked)

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
            totalPage = 1,
            page = 0,
            jobCode = null,
            techCode = null,
        )
    }
}
