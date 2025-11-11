package team.retum.post.review.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.CodeType
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.common.enums.ReviewProcess
import team.retum.usecase.entity.CodesEntity
import team.retum.usecase.entity.MyReviewsEntity.MyReview
import team.retum.usecase.entity.PostReviewEntity.PostReviewContentEntity
import team.retum.usecase.usecase.code.FetchCodeUseCase
import team.retum.usecase.usecase.review.FetchMyReviewUseCase
import team.retum.usecase.usecase.student.FetchStudentInformationUseCase
import javax.inject.Inject

@HiltViewModel
internal class PostReviewViewModel @Inject constructor(
    private val fetchCodeUseCase: FetchCodeUseCase,
    private val fetchMyReviewsUseCase: FetchMyReviewUseCase,
    private val fetchStudentInformationUseCase: FetchStudentInformationUseCase,
) : BaseViewModel<PostReviewState, PostReviewSideEffect>(PostReviewState.getInitialState()) {

    init {
        fetchStudentInfo()
    }
    private val _techs = MutableStateFlow<List<CodesEntity.CodeEntity>>(emptyList())
    val techs = _techs.asStateFlow()

    private fun fetchStudentInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchStudentInformationUseCase().onSuccess {
                setState { state.value.copy(studentName = it.studentName) }
            }
        }
    }

    internal fun setShowExitConfirmDialog(showExitConfirmDialog: Boolean) = setState {
        state.value.copy(showExitConfirmDialog = showExitConfirmDialog)
    }

    internal fun setKeyword(keyword: String?) {
        _techs.value = emptyList()
        setState {
            state.value.copy(
                keyword = keyword ?: "",
            )
        }
    }

    internal fun addReviewProcess(reviewProcess: ReviewProcess) {
        setState {
            state.value.copy(
                reviewProcess = reviewProcess,
            )
        }
    }

    internal fun setSelectedTech(selectedTech: Long?) =
        setState { state.value.copy(selectedTech = selectedTech ?: 0) }

    internal fun fetchCodes(keyword: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            fetchCodeUseCase(
                keyword = keyword,
                type = CodeType.JOB,
                parentCode = null,
            ).onSuccess {
                _techs.value = it.codes
            }
        }

    internal fun fetchMyReview() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchMyReviewsUseCase().onSuccess {
                setState {
                    state.value.copy(
                        myReview = it.reviews,
                    )
                }
            }
        }
    }

    internal fun setChecked(checked: String?) {
        setState {
            state.value.copy(
                checked = checked ?: "",
            )
        }
    }

    internal fun setInterviewType(interviewType: InterviewType?) {
        setState {
            state.value.copy(
                interviewType = interviewType,
            )
        }
    }

    internal fun setInterviewLocation(interviewLocation: InterviewLocation?) {
        setState {
            state.value.copy(
                interviewLocation = interviewLocation,
            )
        }
    }

    internal fun setInterviewerCount(count: String) {
        setState {
            state.value.copy(
                count = count,
            )
        }
    }

    internal fun onNextClick() {
        with(state.value) {
            if (interviewType != null && interviewLocation != null) {
                postSideEffect(
                    PostReviewSideEffect.MoveToNext(
                        companyId = companyId,
                        interviewerCount = count.toInt(),
                        jobCode = selectedTech ?: 0,
                        interviewType = interviewType,
                        location = interviewLocation,
                    ),
                )
            } else {
                postSideEffect(
                    PostReviewSideEffect.SelectTechAndCount,
                )
            }
        }
    }
}

internal data class PostReviewState(
    val studentName: String,
    val interviewType: InterviewType?,
    val interviewLocation: InterviewLocation?,
    val companyId: Long,
    val jobCode: Long,
    val qnaElements: List<PostReviewContentEntity>,
    val question: String,
    val answer: String,
    val keyword: String?,
    val checked: String?,
    val selectedTech: Long?,
    val tech: String?,
    val reviewProcess: ReviewProcess?,
    val count: String,
    val myReview: List<MyReview>,
    val showExitConfirmDialog: Boolean,
    val showModalLossDataDialog: Boolean,
) {
    val buttonEnabled: Boolean
        get() = when (reviewProcess) {
            ReviewProcess.INTERVIEW_TYPE -> interviewType != null
            ReviewProcess.INTERVIEW_LOCATION -> interviewLocation != null
            ReviewProcess.TECH_STACK -> keyword?.isNotEmpty() == true
            ReviewProcess.INTERVIEWER_COUNT -> count.isNotEmpty()
            ReviewProcess.SUMMARY -> true
            null -> false
        }
    companion object {
        fun getInitialState() = PostReviewState(
            studentName = "",
            question = "",
            answer = "",
            keyword = "",
            checked = "",
            selectedTech = 0,
            tech = null,
            reviewProcess = ReviewProcess.INTERVIEW_TYPE,
            interviewType = null,
            interviewLocation = null,
            count = "",
            companyId = 0,
            jobCode = 0,
            qnaElements = emptyList(),
            myReview = emptyList(),
            showExitConfirmDialog = false,
            showModalLossDataDialog = false,
        )
    }
}

internal sealed interface PostReviewSideEffect {
    data class MoveToNext(
        val interviewType: InterviewType,
        val location: InterviewLocation,
        val companyId: Long,
        val jobCode: Long,
        val interviewerCount: Int,
    ) : PostReviewSideEffect
    data object BadRequest : PostReviewSideEffect

    data object SelectTechAndCount : PostReviewSideEffect
}
