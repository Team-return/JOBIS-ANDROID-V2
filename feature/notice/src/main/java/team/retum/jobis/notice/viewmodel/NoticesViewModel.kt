package team.retum.jobis.notice.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.exception.BadRequestException
import team.retum.usecase.entity.notice.NoticesEntity
import team.retum.usecase.usecase.notice.FetchNoticesCountUseCase
import team.retum.usecase.usecase.notice.FetchNoticesUseCase
import javax.inject.Inject

private const val NOTICE_PAGE_SIZE = 12

@HiltViewModel
internal class NoticesViewModel @Inject constructor(
    private val fetchNoticesUseCase: FetchNoticesUseCase,
    private val fetchNoticesCountUseCase: FetchNoticesCountUseCase,
) : BaseViewModel<NoticesState, NoticesSideEffect>(NoticesState.getDefaultState()) {
    private val fetchLock = Any()

    internal fun fetchNotices() {
        val page = synchronized(fetchLock) {
            val currentState = state.value
            if (currentState.isLoading || currentState.totalPage?.let { currentState.page >= it } == true) {
                null
            } else {
                val nextPage = currentState.page + 1
                setState { state.value.copy(isLoading = true) }
                nextPage
            }
        } ?: return

        viewModelScope.launch(Dispatchers.IO) {
            fetchNoticesUseCase(page = page)
                .onSuccess {
                    setState {
                        state.value.copy(
                            page = page,
                            notices = state.value.notices + it.notices,
                            totalPage = state.value.totalPage ?: if (it.notices.size < NOTICE_PAGE_SIZE) page else null,
                            isLoading = false,
                        )
                    }
                }.onFailure {
                    setState { state.value.copy(isLoading = false) }
                    when (it) {
                        is BadRequestException -> {
                            postSideEffect(NoticesSideEffect.BadRequest)
                        }
                    }
                }
        }
    }

    internal fun fetchTotalNoticeCount() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchNoticesCountUseCase()
                .onSuccess {
                    setState {
                        state.value.copy(
                            totalPage = it.totalPageCount,
                            isNoticeCountFetchFailed = false,
                        )
                    }
                    fetchNotices()
                }.onFailure {
                    setState { state.value.copy(isNoticeCountFetchFailed = true) }
                    fetchNotices()
                    when (it) {
                        is BadRequestException -> {
                            postSideEffect(NoticesSideEffect.BadRequest)
                        }
                    }
                }
        }
    }

    internal fun whetherFetchNextPage(lastVisibleItemIndex: Int): Boolean = with(state.value) {
        return !isLoading &&
            lastVisibleItemIndex == notices.lastIndex &&
            (totalPage == null || page < totalPage)
    }
}

internal data class NoticesState(
    val notices: List<NoticesEntity.NoticeEntity>,
    val page: Long,
    val totalPage: Long?,
    val isLoading: Boolean,
    val isNoticeCountFetchFailed: Boolean,
) {
    companion object {
        fun getDefaultState() = NoticesState(
            notices = mutableStateListOf(),
            page = 0,
            totalPage = null,
            isLoading = false,
            isNoticeCountFetchFailed = false,
        )
    }
}

internal sealed interface NoticesSideEffect {
    data object BadRequest : NoticesSideEffect
}
