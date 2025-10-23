package team.retum.review.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.review.model.PostReviewData.PostReviewContent
import team.retum.usecase.usecase.student.FetchStudentInformationUseCase
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
internal class PostReviewCompleteViewModel @Inject constructor(
    private val fetchStudentInformationUseCase: FetchStudentInformationUseCase
): BaseViewModel<PostReviewCompleteState, PostReviewCompleteSideEffect>(PostReviewCompleteState.getInitialState()) {
    init {
        fetchStudentInfo()
    }

    private fun fetchStudentInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchStudentInformationUseCase().onSuccess {
                setState { state.value.copy(studentName = it.studentName) }
            }
        }
    }
}

@Immutable
internal data class PostReviewCompleteState(
    val studentName: String,
)  {
    companion object {
        fun getInitialState() = PostReviewCompleteState(
            studentName = "",
        )
    }
}

internal sealed interface PostReviewCompleteSideEffect {
    data class MoveToNext(
        val qnaElements: List<PostReviewContent>,
    ): PostNextReviewSideEffect
}
