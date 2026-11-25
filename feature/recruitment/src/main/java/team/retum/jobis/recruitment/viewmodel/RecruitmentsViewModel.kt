package team.retum.jobis.recruitment.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.RecruitSortType
import team.retum.common.enums.RecruitmentRegion
import team.retum.common.enums.RecruitmentStatus
import team.retum.jobis.local.entity.BookmarkLocalEntity
import team.retum.usecase.entity.RecruitmentsEntity
import team.retum.usecase.usecase.bookmark.ObserveBookmarkStatusUseCase
import team.retum.usecase.usecase.bookmark.SyncBookmarksFromServerUseCase
import team.retum.usecase.usecase.bookmark.ToggleBookmarkUseCase
import team.retum.usecase.usecase.recruitment.FetchRecruitmentCountUseCase
import team.retum.usecase.usecase.recruitment.FetchRecruitmentsUseCase
import javax.inject.Inject

private const val NUMBER_OF_ITEM_ON_PAGE = 12

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
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
    private val observeBookmarkStatusUseCase: ObserveBookmarkStatusUseCase,
    private val syncBookmarksFromServerUseCase: SyncBookmarksFromServerUseCase,
) : BaseViewModel<RecruitmentsState, RecruitmentsSideEffect>(RecruitmentsState.getDefaultState()) {
    private val _recruitments: SnapshotStateList<RecruitmentsEntity.RecruitmentEntity> =
        mutableStateListOf()
    val recruitments: List<RecruitmentsEntity.RecruitmentEntity> = _recruitments
    private var requestVersion = 0L
    private var isLoading = false

    init {
        debounceName()
        syncBookmarks()
        observeAllBookmarks()
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

    private fun resetRecruitments() {
        _recruitments.clear()
        isLoading = false
        setState {
            state.value.copy(
                page = 0L,
                totalPage = 1L,
                showRecruitmentsEmptyContent = false,
            )
        }
    }

    internal fun setFilters(
        jobCode: Long?,
        techCode: String?,
        years: ImmutableList<Int>?,
        status: RecruitmentStatus?,
        region: RecruitmentRegion?,
        isWinterIntern: Boolean,
    ): Boolean {
        val normalizedTechCode = techCode?.takeIf { it.isNotBlank() }
        val filtersChanged = with(state.value) {
            this.jobCode != jobCode ||
                this.techCode != normalizedTechCode ||
                this.years != years ||
                this.status != status ||
                this.region != region ||
                this.isWinterIntern != isWinterIntern
        }
        if (!filtersChanged) return false

        requestVersion++
        setState {
            state.value.copy(
                jobCode = jobCode,
                techCode = normalizedTechCode,
                years = years,
                status = status,
                region = region,
                isWinterIntern = isWinterIntern,
            )
        }
        resetRecruitments()
        return true
    }

    internal fun setSortType(sortType: RecruitSortType?) {
        if (state.value.sortType == sortType) return

        requestVersion++
        setState {
            state.value.copy(
                sortType = sortType,
            )
        }
        resetRecruitments()
        fetchTotalRecruitmentCount()
    }

    internal fun fetchRecruitments() {
        fetchRecruitments(requestVersion)
    }

    private fun fetchRecruitments(version: Long) {
        if (isLoading) return

        val requestState = state.value
        val nextPage = requestState.page + 1
        if (nextPage > requestState.totalPage) return

        isLoading = true
        addRecruitmentEntities()
        viewModelScope.launch {
            fetchRecruitmentsUseCase(
                name = requestState.name,
                page = nextPage.toInt(),
                jobCode = requestState.jobCode,
                techCode = requestState.techCode,
                winterIntern = requestState.isWinterIntern,
                militarySupport = null,
                years = requestState.years,
                recruitStatus = requestState.status,
                sortType = requestState.sortType,
                region = requestState.region,
            ).onSuccess {
                if (version != requestVersion) return@onSuccess

                removeLoadingRecruitments()
                _recruitments.addAll(it.recruitments)
                isLoading = false
                setState {
                    state.value.copy(
                        page = nextPage,
                        showRecruitmentsEmptyContent = nextPage == 1L && it.recruitments.isEmpty(),
                    )
                }
            }.onFailure {
                if (version != requestVersion) return@onFailure

                removeLoadingRecruitments()
                isLoading = false
                postFetchRecruitmentsErrorIfEmpty()
            }
        }
    }

    /**
     * 모집의뢰서 더미 데이터를 추가하는 함수
     */
    private fun addRecruitmentEntities() {
        repeat(NUMBER_OF_ITEM_ON_PAGE) {
            _recruitments.add(RecruitmentsEntity.RecruitmentEntity.getDefaultEntity())
        }
    }

    private fun removeLoadingRecruitments() {
        _recruitments.removeAll { it.id == 0L }
    }

    internal fun fetchTotalRecruitmentCount() {
        val version = ++requestVersion
        val requestState = state.value
        resetRecruitments()
        viewModelScope.launch {
            fetchRecruitmentCountUseCase.invoke(
                name = requestState.name,
                jobCode = requestState.jobCode,
                techCode = requestState.techCode,
                winterIntern = requestState.isWinterIntern,
                militarySupport = null,
                years = requestState.years,
                recruitStatus = requestState.status,
                sortType = null,
                region = requestState.region,
            ).onSuccess {
                if (version != requestVersion) return@onSuccess

                setState {
                    state.value.copy(
                        totalPage = it.totalPageCount,
                        showRecruitmentsEmptyContent = it.totalPageCount == 0L,
                    )
                }
                if (it.totalPageCount > 0L) {
                    fetchRecruitments(version)
                }
            }.onFailure {
                if (version != requestVersion) return@onFailure

                postFetchRecruitmentsErrorIfEmpty()
            }
        }
    }

    internal fun setName(name: String) {
        requestVersion++
        setState {
            state.value.copy(
                name = name,
            )
        }
        resetRecruitments()
    }

    /**
     * [lastVisibleItemIndex]가 현재 모집의뢰서 목록에서 마지막 아이템인지 확인하는 함수
     *
     * @param lastVisibleItemIndex 화면에 보여지고 있는 마지막 아이템의 인덱스
     * @return 마지막 아이템인지 여부를 반환
     */
    internal fun whetherFetchNextPage(lastVisibleItemIndex: Int): Boolean = with(state.value) {
        return !isLoading && lastVisibleItemIndex == _recruitments.lastIndex && page < totalPage
    }

    internal fun bookmarkRecruitment(recruitmentId: BookmarkLocalEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            toggleBookmarkUseCase(recruitmentId)
        }
    }

    private fun postFetchRecruitmentsErrorIfEmpty() {
        if (_recruitments.none { it.id != 0L }) {
            postSideEffect(RecruitmentsSideEffect.FetchRecruitmentsError)
        }
    }

    private fun syncBookmarks() {
        viewModelScope.launch(Dispatchers.IO) {
            syncBookmarksFromServerUseCase()
        }
    }

    private fun observeAllBookmarks() {
        viewModelScope.launch {
            observeBookmarkStatusUseCase()
                .map { bookmarks -> bookmarks.map { it.recruitmentId }.toSet() }
                .collect { bookmarkedIds ->
                    _recruitments.forEach { recruitment ->
                        if (recruitment.id == 0L) return@forEach
                        updateBookmarkInList(recruitment.id, recruitment.id in bookmarkedIds)
                    }
                }
        }
    }

    private fun updateBookmarkInList(recruitmentId: Long, isBookmarked: Boolean) {
        val index = _recruitments.indexOfFirst { it.id == recruitmentId }
        if (index != -1 && _recruitments[index].bookmarked != isBookmarked) {
            _recruitments[index] = _recruitments[index].copy(bookmarked = isBookmarked)
        }
    }
}

internal data class RecruitmentsState(
    val totalPage: Long,
    val page: Long,
    val jobCode: Long?,
    val techCode: String?,
    val isWinterIntern: Boolean,
    val name: String?,
    val showRecruitmentsEmptyContent: Boolean,
    val years: ImmutableList<Int>?,
    val status: RecruitmentStatus?,
    val region: RecruitmentRegion?,
    val sortType: RecruitSortType?,
) {
    companion object {
        fun getDefaultState() = RecruitmentsState(
            totalPage = 1,
            page = 0,
            jobCode = null,
            techCode = null,
            isWinterIntern = false,
            name = null,
            showRecruitmentsEmptyContent = false,
            years = null,
            status = null,
            region = null,
            sortType = null,
        )
    }
}

internal sealed interface RecruitmentsSideEffect {
    data object FetchRecruitmentsError : RecruitmentsSideEffect
}
