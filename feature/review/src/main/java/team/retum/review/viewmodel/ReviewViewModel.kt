package team.retum.review.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.usecase.entity.FetchReviewsEntity
import team.retum.usecase.usecase.review.FetchReviewsCountUseCase
import team.retum.usecase.usecase.review.FetchReviewsUseCase
import javax.inject.Inject

private const val NUMBER_OF_ITEM_ON_PAGE = 12
private const val LAST_INDEX_OF_PAGE = 11

/**
 * 면접 후기 목록 조회 비즈니스 로직을 담당하는 뷰모델
 *
 * @property fetchReviewsUseCase 면접 후기 조회를 담당하는 유즈케이스
 * @property fetchReviewsCountUseCase 면접 후기 전체 페이지 갯수 조회를 담당하는 유즈케이스
 */
@HiltViewModel
internal class ReviewViewModel @Inject constructor(
    private val fetchReviewsUseCase: FetchReviewsUseCase,
    private val fetchReviewsCountUseCase: FetchReviewsCountUseCase,
) : BaseViewModel<ReviewState, ReviewSideEffect>(ReviewState.getInitialState()) {

    private val _reviews: SnapshotStateList<FetchReviewsEntity.Review> = mutableStateListOf()
    val reviews: List<FetchReviewsEntity.Review> = _reviews

    init {
        clear()
    }

    private fun clear() {
        _reviews.clear()
        setState {
            state.value.copy(
                page = 0L,
            )
        }
    }

    internal fun setCode(code: Long?) = setState {
        state.value.copy(code = code)
    }

    internal fun setYear(year: Int?) = setState {
        state.value.copy(year = year)
    }

    internal fun setInterviewType(interviewType: InterviewType?) = setState {
        state.value.copy(interviewType = interviewType)
    }

    internal fun setLocation(location: InterviewLocation?) = setState {
        state.value.copy(location = location)
    }

    internal fun clearReviews() {
        if (state.value.code != null || state.value.year != null) {
            _reviews.clear()
            setState { state.value.copy(page = 0L) }
        }
    }

    internal fun fetchReviews() {
        addReviewEntities()
        addPage()
        viewModelScope.launch(Dispatchers.IO) {
            with(state.value) {
                fetchReviewsUseCase(
                    page = page.toInt(),
                    location = location,
                    interviewType = interviewType,
                    companyId = null,
                    keyword = null,
                    year = year,
                    code = code,
                ).onSuccess {
                    replaceReviews(it.reviews)
                }.onFailure {
                    postSideEffect(ReviewSideEffect.FetchError)
                }
            }
        }
    }

    private fun addPage() = setState {
        state.value.copy(page = state.value.page + 1)
    }

    /**
     * 리뷰 더미 데이터를 추가하는 함수
     */
    private fun addReviewEntities() {
        repeat(NUMBER_OF_ITEM_ON_PAGE) {
            _reviews.add(FetchReviewsEntity.Review.getDefaultEntity())
        }
    }

    /**
     * 리뷰 더미 데이터를 서버로부터 받아온 리뷰 데이터로 교체하는 작업을 진행하는 함수
     *
     * @param reviews 서버로부터 받아온 리뷰 데이터
     */
    private fun replaceReviews(reviews: List<FetchReviewsEntity.Review>) {
        val startIndex = _reviews.lastIndex - LAST_INDEX_OF_PAGE
        runCatching {
            reviews.forEachIndexed { index, review ->
                _reviews[startIndex + index] = review
            }
            _reviews.removeAll(_reviews.filter { item -> item.reviewId == 0L })
        }.onFailure {
            postSideEffect(ReviewSideEffect.FetchError)
        }
    }

    internal fun fetchTotalReviewCount() {
        with(state.value) {
            viewModelScope.launch(Dispatchers.IO) {
                fetchReviewsCountUseCase.invoke(
                    location = location,
                    interviewType = interviewType,
                    keyword = null,
                    year = year,
                    code = code,
                ).onSuccess {
                    setState { copy(totalPage = it.totalPageCount) }
                    fetchReviews()
                }.onFailure {
                    postSideEffect(ReviewSideEffect.FetchError)
                }
            }
        }
    }

    /**
     * [lastVisibleItemIndex]가 현재 리뷰 목록에서 마지막 아이템인지 확인하는 함수
     *
     * @param lastVisibleItemIndex 화면에 보여지고 있는 마지막 아이템의 인덱스
     * @return 마지막 아이템인지 여부를 반환
     */
    internal fun whetherFetchNextPage(lastVisibleItemIndex: Int): Boolean = with(state.value) {
        return lastVisibleItemIndex == _reviews.lastIndex && page < totalPage
    }
}

internal data class ReviewState(
    val totalPage: Long,
    val page: Long,
    val companyId: Long,
    val code: Long?,
    val year: Int?,
    val interviewType: InterviewType?,
    val location: InterviewLocation?,
) {
    companion object {
        fun getInitialState() = ReviewState(
            totalPage = 1,
            page = 0L,
            companyId = 0,
            code = null,
            year = null,
            interviewType = null,
            location = null,
        )
    }
}

internal sealed class ReviewSideEffect {
    data object FetchError : ReviewSideEffect()
}
