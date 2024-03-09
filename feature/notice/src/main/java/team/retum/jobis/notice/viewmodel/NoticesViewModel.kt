package team.retum.jobis.notice.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.entity.notice.NoticesEntity
import team.retum.usecase.usecase.notice.FetchNoticesUseCase
import javax.inject.Inject

@HiltViewModel
internal class NoticesViewModel @Inject constructor(
    private val fetchNoticesUseCase: FetchNoticesUseCase,
) : BaseViewModel<NoticesState, Unit>(NoticesState.getDefaultState()) {

    internal var notices: SnapshotStateList<NoticesEntity.NoticeEntity> =
        mutableStateListOf()
        private set

    internal fun fetchNotices(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            with(state.value) {
                fetchNoticesUseCase(page = page)
                    .onSuccess {
                        notices.addAll(it.notices)
                    }
            }

        }
    }
}

internal data class NoticesState(
    val page: Int,
) {
    companion object {
        fun getDefaultState() = NoticesState(
            page = 0,
        )
    }
}
