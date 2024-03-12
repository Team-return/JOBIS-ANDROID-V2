package team.retum.jobis.notice.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.exception.BadRequestException
import team.retum.usecase.entity.notice.NoticesEntity
import team.retum.usecase.usecase.notice.FetchNoticesUseCase
import javax.inject.Inject

@HiltViewModel
internal class NoticesViewModel @Inject constructor(
    private val fetchNoticesUseCase: FetchNoticesUseCase,
) : BaseViewModel<NoticesState, NoticesSideEffect>(NoticesState.getDefaultState()) {
    internal fun fetchNotices() {
        viewModelScope.launch(Dispatchers.IO) {
            with(state.value) {
                fetchNoticesUseCase()
                    .onSuccess {
                        setState { state.value.copy(notices = it.notices) }
                    }
                    .onFailure {
                        when (it) {
                            is BadRequestException -> {
                                postSideEffect(NoticesSideEffect.BadRequest)
                            }
                        }
                    }
            }
        }
    }
}

internal data class NoticesState(
    val noticeId: Long,
    val notices: List<NoticesEntity.NoticeEntity>,
) {
    companion object {
        fun getDefaultState() = NoticesState(
            noticeId = 0,
            notices = mutableStateListOf(),
        )
    }
}

internal sealed interface NoticesSideEffect {
    data object BadRequest : NoticesSideEffect
}
