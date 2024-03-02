package team.retum.jobis.recruitment.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.entity.RecruitmentsEntity
import team.retum.usecase.usecase.bookmark.BookmarkRecruitmentUseCase
import team.retum.usecase.usecase.recruitment.FetchRecruitmentCountUseCase
import team.retum.usecase.usecase.recruitment.FetchRecruitmentsUseCase
import javax.inject.Inject

const val SEARCH_DEBOUNCE_MILLIS = 1000L

@HiltViewModel
internal class SearchRecruitmentViewModel @Inject constructor(
    private val fetchRecruitmentsUseCase: FetchRecruitmentsUseCase,
    private val fetchRecruitmentCountUseCase: FetchRecruitmentCountUseCase,
    private val recruitmentBookmarkUseCase: BookmarkRecruitmentUseCase,
) : BaseViewModel<SearchRecruitmentState, Unit>(SearchRecruitmentState.getInitialState()) {

    internal var recruitments: SnapshotStateList<RecruitmentsEntity.RecruitmentEntity> =
        mutableStateListOf()
        private set

    @OptIn(FlowPreview::class)
    internal fun observeName() {
        viewModelScope.launch {
            state.map { it.name }.debounce(SEARCH_DEBOUNCE_MILLIS).collect {
                if (!it.isNullOrBlank()) {
                    initState()
                    fetchRecruitmentCount()
                    fetchRecruitments()
                }
            }
        }
    }

    private fun initState() = setState {
        recruitments.clear()
        state.value.copy(
            page = 1,
            runPaging = true,
        )
    }

    internal fun setName(name: String) = setState {
        recruitments.clear()
        state.value.copy(name = name)
    }

    private fun fetchRecruitments() {
        with(state.value) {
            viewModelScope.launch(Dispatchers.IO) {
                fetchRecruitmentsUseCase(
                    name = name,
                    page = page.toInt(),
                    jobCode = null,
                    techCode = null,
                    winterIntern = false,
                ).onSuccess {
                    recruitments.addAll(it.recruitments)
                }
            }
        }
    }

    private fun fetchRecruitmentCount() {
        viewModelScope.launch(Dispatchers.IO) {
            with(state.value) {
                fetchRecruitmentCountUseCase.invoke(
                    name = name,
                    jobCode = null,
                    techCode = null,
                    winterIntern = false,
                ).onSuccess {
                    setState { copy(totalPage = it.totalPageCount) }
                }
            }
        }
    }

    internal fun Flow<Int?>.callNextPageByPosition() {
        viewModelScope.launch {
            val fetchNextPage = async {
                this@callNextPageByPosition.collect {
                    it?.run {
                        if (this == recruitments.lastIndex && !state.value.name.isNullOrBlank() && state.value.runPaging) {
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
                        setState { state.value.copy(runPaging = false) }
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

internal data class SearchRecruitmentState(
    val totalPage: Long,
    val name: String?,
    val page: Long,
    val runPaging: Boolean,
) {
    companion object {
        fun getInitialState() = SearchRecruitmentState(
            totalPage = 0,
            name = null,
            page = 0,
            runPaging = true,
        )
    }
}
