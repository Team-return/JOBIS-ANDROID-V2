package team.retum.bookmark.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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

    init {
        fetchBookmarks()
    }

    private val _bookmarks: MutableState<BookmarksEntity> =
        mutableStateOf(BookmarksEntity(emptyList()))
    val bookmarks: MutableState<BookmarksEntity> get() = _bookmarks

    private fun fetchBookmarks() {
        viewModelScope.launch(Dispatchers.IO) {
            bookmarkUseCase().onSuccess {
                _bookmarks.value = it
            }.onFailure {
                when (it) {
                    is BadRequestException -> {
                        postSideEffect(BookmarkSideEffect.BadRequest)
                    }
                }
            }
        }
    }

    fun bookmarkRecruitment(recruitmentId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            recruitmentBookmarkUseCase(recruitmentId).onFailure {
                when (it) {
                    is BadRequestException -> {
                        postSideEffect(BookmarkSideEffect.BadRequest)
                    }
                }
            }
            fetchBookmarks()
        }
    }
}

internal sealed interface BookmarkSideEffect {
    data object BadRequest : BookmarkSideEffect
}
