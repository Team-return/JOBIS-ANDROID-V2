package team.retum.post.review.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.CodeType
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.common.enums.ReviewProcess
import team.retum.common.exception.BadRequestException
import team.retum.post.review.model.PostReviewData
import team.retum.post.review.model.toEntity
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

    /**
     * Fetches the current student's information and updates the state's `studentName` when successful.
     */
    private fun fetchStudentInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchStudentInformationUseCase().onSuccess {
                setState { state.value.copy(studentName = it.studentName) }
            }
        }
    }

    /**
     * Updates the current state's question text and re-evaluates the action button enabled state.
     *
     * @param question The new question text to store in the view state.
     */
    internal fun setQuestion(question: String) {
        setState { state.value.copy(question = question) }
        setButtonEnabled()
    }

    /**
     * Sets the current answer for the review and recomputes whether the action button should be enabled.
     *
     * @param answer The answer text to store in the view state.
     */
    internal fun setAnswer(answer: String) {
        setState { state.value.copy(answer = answer) }
        setButtonEnabled()
    }

    /**
     * Updates the state's keyword, clears current tech suggestions, and enables the action button.
     *
     * @param keyword The new keyword to set; if `null`, the state's keyword is set to an empty string.
     */
    internal fun setKeyword(keyword: String?) {
        techs.clear()
        setState {
            state.value.copy(
                keyword = keyword ?: "",
                buttonEnabled = true
            )
        }
    }

    /**
     * Submits the given review data to the server and emits side effects reflecting the outcome.
     *
     * On successful submission emits [PostReviewSideEffect.Success]; if the request fails with
     * [BadRequestException] emits [PostReviewSideEffect.BadRequest].
     *
     * @param reviewData The review details used to build the request payload.
     */
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
/**
         * Updates the state's selectedTech with the given technology ID or clears it when null.
         *
         * @param selectedTech The selected technology ID, or `null` to reset the selection to `0`.
         */

    internal fun setSelectedTech(selectedTech: Long?) =
        setState { state.value.copy(selectedTech = selectedTech ?: 0)  }

    /**
         * Starts fetching job-type codes filtered by the given keyword and appends the resulting codes to `techs`.
         *
         * @param keyword An optional search term to filter codes; if `null`, codes are fetched without a keyword filter.
         * @return The coroutine [Job] representing the launched fetch operation.
         */
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

    /**
     * Fetches the current user's reviews and updates the view state with the retrieved list.
     *
     * Invokes FetchMyReviewUseCase and, on success, sets `state.myReview` to the fetched reviews.
     */
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

    /**
     * Updates the state's `buttonEnabled` flag according to the current review process.
     *
     * For `ReviewProcess.INTERVIEW_TYPE`, enables the button when both `question` and `answer` are non-empty.
     * For other review processes, enables the button when `keyword` is null or empty.
     */
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

    /**
     * Updates the state's `checked` value and enables the action button.
     *
     * If `checked` is `null`, the state's `checked` field is set to an empty string.
     *
     * @param checked The new checked value or `null` to clear it.
     */
    internal fun setChecked(checked: String?) {
        setState {
            state.value.copy(
                checked = checked ?: "",
                buttonEnabled = true,
            )
        }
    }

    /**
     * Sets the interview type for the current post-review state and enables the action button.
     *
     * @param interviewType The selected interview type to store in state.
     */
    internal fun setInterviewType(interviewType: InterviewType) {
        setState {
            state.value.copy(
                interviewType = interviewType,
                buttonEnabled = true,
            )
        }
    }

    /**
     * Updates the state's interview location and enables the action button.
     *
     * @param interviewLocation The selected interview location to store in state.
     */
    internal fun setInterviewLocation(interviewLocation: InterviewLocation) {
        setState {
            state.value.copy(
                interviewLocation = interviewLocation,
                buttonEnabled = true,
            )
        }
    }

    /**
     * Sets the interviewer count in the current post-review state and enables the action button.
     *
     * @param count The interviewer count as entered (a string representation of the number, e.g. "1").
     */
    internal fun setInterviewerCount(count: String) {
        //techs.clear()
        setState {
            state.value.copy(
                count = count,
                buttonEnabled = true,
            )
        }
    }

    /**
     * Disables the primary action button in the current view state.
     *
     * Sets `buttonEnabled` to `false`.
     */
    internal fun setButtonClear() {
        setState {
            state.value.copy(
                buttonEnabled = false,
            )
        }
    }

    /**
     * Emits a MoveToNext side effect carrying the current interview details to advance the posting workflow.
     *
     * The emitted side effect includes:
     * - `interviewType` and `location` from the current state,
     * - `companyId` from the current state,
     * - `interviewerCount` parsed from the state's `count` string,
     * - `jobCode` taken from `selectedTech` or `0` if `selectedTech` is null.
     */
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
        /**
         * Create an initial PostReviewState populated with default values for the post-review flow.
         *
         * @return A PostReviewState with empty strings for text fields, `selectedTech` set to 0, `tech` null,
         * `buttonEnabled` false, `reviewProcess` set to `ReviewProcess.INTERVIEW_TYPE`, default enum
         * selections (`InterviewType.INDIVIDUAL`, `InterviewLocation.DAEJEON`), zeros for numeric IDs and counts,
         * and empty lists for `qnaElements` and `myReview`.
         */
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