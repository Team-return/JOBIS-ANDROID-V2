package team.retum.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * TODO
 *
 * @param S 상속받는 뷰모델에서 사용될 state data class
 * @param E 상속받는 뷰모델에서 사용할 side effect sealed class
 * @constructor
 * 상속받는 뷰모델에서 사용할 초기 상태의 data class를 인자로 받는다.
 */
abstract class BaseViewModel<S, E>(initialState: S) : ViewModel() {
    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    private val _sideEffect: MutableSharedFlow<E> = MutableSharedFlow()
    val sideEffect = _sideEffect.asSharedFlow()

    /**
     * JOBIS에서 [state]의 상태를 업데이트 하기 위해 사용하는 함수
     *
     * @param newState 새로운 상태를 반환하는 함수
     *
     * 함수 내부에서 반환값(마지막 줄)을 [state]로 작성할 수 있는 경우 다음과 같은 방식으로 사용한다.
     * ```
     * internal fun setRecruitmentId(recruitmentId: Long) = setState {
     *     state.value.copy(recruitmentId = recruitmentId)
     * }
     * ```
     * 위 경우에 해당하지 않는다면 다음과 같은 방식으로 사용한다.
     * ```
     * internal fun setEmail(email: String) {
     *     setState {
     *         state.value.copy(
     *             email = email,
     *             showEmailDescription = false,
     *         )
     *     }
     *     setButtonEnabled()
     * }
     * ```
     */
    protected fun setState(newState: () -> S) {
        _state.update {
            newState()
        }
    }

    /**
     * JOBIS에서 [sideEffect]를 발생시키기 위해 사용하는 함수
     *
     * @param sideEffect 방출할 sideEffect
     *
     * [sideEffect]가 예외 처리시 반드시 방출되어야 하고, 예외에 따라 [sideEffect] 클래스만 변경되는 경우라면 다음과 같은 방식으로 사용한다.
     * ```
     * postSideEffect(
     *     sideEffect = when (isReApply) {
     *         true -> ApplicationSideEffect.SuccessReApply
     *         else -> ApplicationSideEffect.SuccessApply
     *     },
     * )
     * ```
     * 위 경우에 해당하지 않는다면 다음과 같은 방식으로 사용한다.
     * ```
     * when (it) {
     *     is BadRequestException -> {
     *         postSideEffect(SignInSideEffect.BadRequest)
     *     }
     *
     *     is OfflineException -> {
     *         postSideEffect(SignInSideEffect.CheckInternetConnection)
     *     }
     *
     *     is ConnectionTimeOutException -> {
     *         postSideEffect(SignInSideEffect.ServerTimeOut)
     *     }
     *
     *     else -> {
     *         setState {
     *             state.value.copy(
     *                 showEmailDescription = it is NotFoundException,
     *                 showPasswordDescription = it is UnAuthorizedException,
     *             )
     *         }
     *     }
     * }
     * ```
     */
    protected fun postSideEffect(sideEffect: E) {
        viewModelScope.launch(Dispatchers.IO) {
            _sideEffect.emit(sideEffect)
        }
    }
}
