package team.retum.bookmark.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.exception.BadRequestException
import team.retum.usecase.entity.BookmarksEntity
import team.retum.usecase.usecase.bookmark.BookmarkRecruitmentUseCase
import team.retum.usecase.usecase.bookmark.BookmarkUseCase
import javax.inject.Inject

@HiltViewModel
internal class BookmarkViewModel @Inject constructor(
    private val bookmarkUseCase: BookmarkUseCase,
    private val recruitmentBookmarkUseCase: BookmarkRecruitmentUseCase,
) : BaseViewModel<Unit, BookmarkSideEffect>(Unit) {

    private val _bookmarks: SnapshotStateList<BookmarksEntity.BookmarkEntity> = mutableStateListOf()
    internal val bookmarks: List<BookmarksEntity.BookmarkEntity> = _bookmarks

    internal fun fetchBookmarks() {
        viewModelScope.launch(Dispatchers.IO) {
            bookmarkUseCase().onSuccess {
                _bookmarks.addAll(it.bookmarks)
            }.onFailure {
                when (it) {
                    is BadRequestException -> {
                        postSideEffect(BookmarkSideEffect.BadRequest)
                    }
                }
            }
        }
    }

    internal fun clearBookmarks() {
        _bookmarks.clear()
    }

    internal fun bookmarkRecruitment(recruitmentId: Long) {
        _bookmarks.removeAt(_bookmarks.indexOf(_bookmarks.find { it.recruitmentId == recruitmentId }))
        viewModelScope.launch(Dispatchers.IO) {
            recruitmentBookmarkUseCase(recruitmentId).onFailure {
                when (it) {
                    is BadRequestException -> {
                        postSideEffect(BookmarkSideEffect.BadRequest)
                    }
                }
            }
        }
    }
}

internal sealed interface BookmarkSideEffect {
    data object BadRequest : BookmarkSideEffect
}
