package team.retum.jobis.recruitment.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.entity.RecruitmentsEntity
import team.retum.usecase.usecase.bookmark.BookmarkRecruitmentUseCase
import team.retum.usecase.usecase.recruitment.FetchRecruitmentCountUseCase
import team.retum.usecase.usecase.recruitment.FetchRecruitmentsUseCase
import javax.inject.Inject

private const val NUMBER_OF_ITEM_ON_PAGE = 12
private const val LAST_INDEX_OF_PAGE = 11

/**
 * 모집의뢰서 목록 조회 및 검색 비즈니스 로직을 담당하는 뷰모델
 *
 * @property fetchRecruitmentsUseCase 모집의뢰서 조회를 담당하는 유즈케이스
 * @property fetchRecruitmentCountUseCase 모집의뢰서 전체 페이지 갯수 조회를 담당하는 유즈케이스
 * @property recruitmentBookmarkUseCase 모집의뢰서 북마크를 담당하는 유즈케이스
 */
@HiltViewModel
internal class RecruitmentViewModel @Inject constructor(
    private val fetchRecruitmentsUseCase: FetchRecruitmentsUseCase,
    private val fetchRecruitmentCountUseCase: FetchRecruitmentCountUseCase,
    private val recruitmentBookmarkUseCase: BookmarkRecruitmentUseCase,
) : BaseViewModel<RecruitmentsState, RecruitmentsSideEffect>(RecruitmentsState.getDefaultState()) {

    private val _recruitments: SnapshotStateList<RecruitmentsEntity.RecruitmentEntity> =
        mutableStateListOf()
    val recruitments: List<RecruitmentsEntity.RecruitmentEntity> = _recruitments

    init {
        clear()
        debounceName()
    }

    /**
     * 매 입력마다 서버 요청이 수행되는 것을 방지하기 위해 검색어에 대해 debounce를 적용하여 모집의뢰서 조회 함수를 호출하는 함수
     * - name을 [SEARCH_DEBOUNCE_MILLIS]마다 관찰하여 모집의뢰서 조회 함수 호출
     */
    @OptIn(FlowPreview::class)
    private fun debounceName() {
        viewModelScope.launch {
            state.map { it.name }.distinctUntilChanged().debounce(SEARCH_DEBOUNCE_MILLIS).collect {
                if (!it.isNullOrBlank()) {
                    fetchTotalRecruitmentCount()
                }
            }
        }
    }

    internal fun clearRecruitment() {
        if (state.value.jobCode != null || state.value.techCode != null) {
            _recruitments.clear()
            setState { state.value.copy(page = 0L) }
        }
    }

    private fun clear() {
        RecruitmentFilterViewModel.jobCode = null
        RecruitmentFilterViewModel.techCode = null
        _recruitments.clear()
        setState {
            state.value.copy(
                page = 0L,
            )
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
                    name = name,
                    page = page.toInt(),
                    jobCode = jobCode,
                    techCode = techCode,
                    winterIntern = false,
                ).onSuccess {
                    setState { state.value.copy(showRecruitmentsEmptyContent = it.recruitments.isEmpty()) }
                    replaceRecruitments(it.recruitments)
                }
            }
        }
    }

    private fun addPage() = setState {
        state.value.copy(page = state.value.page + 1)
    }

    /**
     * 모집의뢰서 더미 데이터를 추가하는 함수
     */
    private fun addRecruitmentEntities() {
        repeat(NUMBER_OF_ITEM_ON_PAGE) {
            _recruitments.add(RecruitmentsEntity.RecruitmentEntity.getDefaultEntity())
        }
    }

    /**
     * 모집의뢰서 더미 데이터를 서버로부터 받아온 모집의뢰서 데이터로 교체하는 작업을 진행하는 함수
     *
     * @param recruitments 서버로부터 받아온 모집의뢰서 데이터
     */
    private fun replaceRecruitments(recruitments: List<RecruitmentsEntity.RecruitmentEntity>) {
        val startIndex = _recruitments.lastIndex - LAST_INDEX_OF_PAGE
        runCatching {
            recruitments.forEachIndexed { index, recruitmentEntity ->
                _recruitments[startIndex + index] = recruitmentEntity
            }
            _recruitments.removeAll(_recruitments.filter { item -> item.id == 0L })
        }.onFailure {
            postSideEffect(RecruitmentsSideEffect.FetchRecruitmentsError)
        }
    }

    internal fun fetchTotalRecruitmentCount() {
        with(state.value) {
            if (page < totalPage) {
                viewModelScope.launch(Dispatchers.IO) {
                    fetchRecruitmentCountUseCase.invoke(
                        name = name,
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
    }

    internal fun setName(name: String) {
        val initialState = RecruitmentsState.getDefaultState()
        _recruitments.clear()
        setState {
            state.value.copy(
                name = name,
                page = initialState.page,
                totalPage = initialState.totalPage,
            )
        }
    }

    /**
     * [lastVisibleItemIndex]가 현재 모집의뢰서 목록에서 마지막 아이템인지 확인하는 함수
     *
     * @param lastVisibleItemIndex 화면에 보여지고 있는 마지막 아이템의 인덱스
     * @return 마지막 아이템인지 여부를 반환
     */
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
    val name: String?,
    val showRecruitmentsEmptyContent: Boolean,
) {
    companion object {
        fun getDefaultState() = RecruitmentsState(
            totalPage = 1,
            page = 0,
            jobCode = null,
            techCode = null,
            name = null,
            showRecruitmentsEmptyContent = false,
        )
    }
}

internal sealed interface RecruitmentsSideEffect {
    data object FetchRecruitmentsError : RecruitmentsSideEffect
}
