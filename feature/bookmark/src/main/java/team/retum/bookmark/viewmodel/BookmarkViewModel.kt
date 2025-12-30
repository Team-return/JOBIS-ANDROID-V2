package team.retum.bookmark.viewmodel

import androidx.lifecycle.viewModelScope
import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.exception.BadRequestException
import team.retum.usecase.entity.BookmarksEntity
import team.retum.usecase.usecase.bookmark.ObserveAllBookmarksUseCase
import team.retum.usecase.usecase.bookmark.SyncBookmarksFromServerUseCase
import team.retum.usecase.usecase.bookmark.ToggleBookmarkUseCase
import javax.inject.Inject

@HiltViewModel
internal class BookmarkViewModel @Inject constructor(
    private val observeAllBookmarksUseCase: ObserveAllBookmarksUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
    private val syncBookmarksFromServerUseCase: SyncBookmarksFromServerUseCase,
) : BaseViewModel<BookmarkState, BookmarkSideEffect>(BookmarkState()) {
    private val _bookmarks = MutableStateFlow<List<BookmarksEntity.BookmarkEntity>>(emptyList())
    val bookmarks: StateFlow<List<BookmarksEntity.BookmarkEntity>> = _bookmarks.asStateFlow()

    init {
        syncBookmarks()
        observeBookmarks()
    }

    internal fun bookmarkRecruitment(recruitmentId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            toggleBookmarkUseCase(recruitmentId).onFailure {
                when (it) {
                    is BadRequestException -> {
                        postSideEffect(BookmarkSideEffect.BadRequest)
                    }
                }
            }
        }
    }

    private fun observeBookmarks() {
        viewModelScope.launch {
            observeAllBookmarksUseCase()
                .collect { bookmarkList ->
                    _bookmarks.value = bookmarkList
                }
        }
    }

    private fun syncBookmarks() {
        viewModelScope.launch(Dispatchers.IO) {
            syncBookmarksFromServerUseCase().onFailure {
                when (it) {
                    is BadRequestException -> {
                        postSideEffect(BookmarkSideEffect.BadRequest)
                    }
                }
            }
        }
    }
}

data class BookmarkState(
    val bookmarks: List<BookmarksEntity.BookmarkEntity> = emptyList(),
)

internal sealed interface BookmarkSideEffect {
    data object BadRequest : BookmarkSideEffect
}
