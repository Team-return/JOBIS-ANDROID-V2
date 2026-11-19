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

@HiltViewModel
internal class NoticesViewModel @Inject constructor(
    private val fetchNoticesUseCase: FetchNoticesUseCase,
    private val fetchNoticesCountUseCase: FetchNoticesCountUseCase,
) : BaseViewModel<NoticesState, NoticesSideEffect>(NoticesState.getDefaultState()) {
    internal fun fetchNotices() {
        val currentState = state.value
        if (currentState.isLoading || currentState.page >= currentState.totalPage) return

        val page = currentState.page + 1
        setState { state.value.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            fetchNoticesUseCase(page = page)
                .onSuccess {
                    setState {
                        state.value.copy(
                            page = page,
                            notices = state.value.notices + it.notices,
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
                    setState { state.value.copy(totalPage = it.totalPageCount) }
                    fetchNotices()
                }.onFailure {
                    when (it) {
                        is BadRequestException -> {
                            postSideEffect(NoticesSideEffect.BadRequest)
                        }
                    }
                }
        }
    }

    internal fun whetherFetchNextPage(lastVisibleItemIndex: Int): Boolean = with(state.value) {
        return !isLoading && lastVisibleItemIndex == notices.lastIndex && page < totalPage
    }
}

internal data class NoticesState(
    val notices: List<NoticesEntity.NoticeEntity>,
    val page: Long,
    val totalPage: Long,
    val isLoading: Boolean,
) {
    companion object {
        fun getDefaultState() = NoticesState(
            notices = mutableStateListOf(),
            page = 0,
            totalPage = 0,
            isLoading = false,
        )
    }
}

internal sealed interface NoticesSideEffect {
    data object BadRequest : NoticesSideEffect
}
