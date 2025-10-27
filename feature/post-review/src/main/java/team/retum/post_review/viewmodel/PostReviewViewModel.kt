package team.retum.post_review.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.CodeType
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.common.enums.ReviewProcess
import team.retum.common.exception.BadRequestException
import team.retum.post_review.model.PostReviewData
import team.retum.post_review.model.toEntity
import team.retum.usecase.entity.CodesEntity
import team.retum.usecase.entity.MyReviewsEntity.MyReview
import team.retum.usecase.entity.PostReviewEntity
import team.retum.usecase.entity.PostReviewEntity.PostReviewContentEntity
import team.retum.usecase.usecase.code.FetchCodeUseCase
import team.retum.usecase.usecase.review.FetchMyReviewUseCase
import team.retum.usecase.usecase.review.PostReviewUseCase
import team.retum.usecase.usecase.student.FetchStudentInformationUseCase
import javax.inject.Inject

@HiltViewModel
internal class PostReviewViewModel @Inject constructor(
    private val fetchCodeUseCase: FetchCodeUseCase,
    private val fetchMyReviewsUseCase: FetchMyReviewUseCase,
    private val fetchStudentInformationUseCase: FetchStudentInformationUseCase,
    private val postReviewUseCase: PostReviewUseCase,
) : BaseViewModel<PostReviewState, PostReviewSideEffect>(PostReviewState.getInitialState()) {

    init {
        fetchStudentInfo()
    }
    var techs: SnapshotStateList<CodesEntity.CodeEntity> = mutableStateListOf()
        private set

    private fun fetchStudentInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchStudentInformationUseCase().onSuccess {
                setState { state.value.copy(studentName = it.studentName) }
            }
        }
    }

    internal fun setQuestion(question: String) {
        setState { state.value.copy(question = question) }
        setButtonEnabled()
    }

    internal fun setAnswer(answer: String) {
        setState { state.value.copy(answer = answer) }
        setButtonEnabled()
    }

    internal fun setKeyword(keyword: String?) {
        techs.clear()
        setState {
            state.value.copy(
                keyword = keyword ?: "",
                buttonEnabled = true
            )
        }
    }

    internal fun postReview(reviewData: PostReviewData) {
        viewModelScope.launch(Dispatchers.IO) {
            postReviewUseCase(
                postReviewRequest = PostReviewEntity(
                    interviewType = reviewData.interviewType,
                    location = reviewData.location,
                    companyId = reviewData.companyId,
                    jobCode = reviewData.jobCode,
                    interviewerCount = reviewData.interviewerCount,
                    qnaElements = reviewData.qnaElements.map { it.toEntity() },
                    question = reviewData.question,
                    answer = reviewData.answer,
                )
            ).onSuccess {
                postSideEffect(PostReviewSideEffect.Success)
            }.onFailure {
                when (it) {
                    is BadRequestException -> {
                        postSideEffect(PostReviewSideEffect.BadRequest)
                    }
                }
            }
        }
    }

//    internal fun addReview() {
//        reviews.add(
//            PostReviewEntity.PostReviewContentEntity(
//                answer = state.value.answer,
//                question = state.value.question,
//                codeId = state.value.selectedTech!!,
//            ),
//        )
//    }

    internal fun setSelectedTech(selectedTech: Long?) =
        setState { state.value.copy(selectedTech = selectedTech ?: 0)  }

    internal fun fetchCodes(keyword: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            fetchCodeUseCase(
                keyword = keyword,
                type = CodeType.JOB,
                parentCode = null,
            ).onSuccess {
                techs.addAll(it.codes)
            }
        }

    internal fun fetchMyReview() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchMyReviewsUseCase().onSuccess {
                setState {
                    state.value.copy(
                        myReview = it.reviews
                    )
                }
            }
        }
    }

    private fun setButtonEnabled() {
        when (state.value.reviewProcess) {
            ReviewProcess.INTERVIEW_TYPE -> {
                setState { state.value.copy(buttonEnabled = state.value.question.isNotEmpty() && state.value.answer.isNotEmpty()) }
            }

            else -> {
               setState { state.value.copy(buttonEnabled = !state.value.keyword?.isNotEmpty()!!) }
            }
        }
    }

    internal fun setChecked(checked: String?) {
        setState {
            state.value.copy(
                checked = checked ?: "",
                buttonEnabled = true,
            )
        }
    }

    internal fun setInterviewType(interviewType: InterviewType) {
        setState {
            state.value.copy(
                interviewType = interviewType,
                buttonEnabled = true,
            )
        }
    }

    internal fun setInterviewLocation(interviewLocation: InterviewLocation) {
        setState {
            state.value.copy(
                interviewLocation = interviewLocation,
                buttonEnabled = true,
            )
        }
    }

    internal fun setInterviewerCount(count: String) {
        //techs.clear()
        setState {
            state.value.copy(
                count = count,
                buttonEnabled = true,
            )
        }
    }

    internal fun setButtonClear() {
        setState {
            state.value.copy(
                buttonEnabled = false,
            )
        }
    }

    internal fun onNextClick() {
        with(state.value) {
            postSideEffect(PostReviewSideEffect.MoveToNext(
                companyId = companyId,
                interviewerCount = count.toInt(),
                jobCode = selectedTech ?: 0,
                interviewType = interviewType,
                location = interviewLocation,
            ))
        }
    }
}

internal data class PostReviewState(
    val studentName: String,
    val interviewType: InterviewType,
    val interviewLocation: InterviewLocation,
    val companyId: Long,
    val jobCode: Long,
    val interviewerCount: Int,
    val qnaElements: List<PostReviewContentEntity>,
    val question: String,
    val answer: String,
    val keyword: String?,
    val checked: String?,
    val selectedTech: Long?,
    val tech: String?,
    val buttonEnabled: Boolean,
    val reviewProcess: ReviewProcess,
    val count: String,
    val myReview: List<MyReview>,
) {
    companion object {
        fun getInitialState() = PostReviewState(
            studentName = "",
            question = "",
            answer = "",
            keyword = "",
            checked = "",
            selectedTech = 0,
            tech = null,
            buttonEnabled = false,
            reviewProcess = ReviewProcess.INTERVIEW_TYPE,
            interviewType = InterviewType.INDIVIDUAL,
            interviewLocation = InterviewLocation.DAEJEON, // TODO :: 널처리(선택 해제) 고려
            count = "",
            companyId = 0,
            jobCode = 0,
            interviewerCount = 0,
            qnaElements = emptyList(),
            myReview = emptyList(),
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
    ): PostReviewSideEffect
    data object BadRequest : PostReviewSideEffect

    data object Success : PostReviewSideEffect
}
