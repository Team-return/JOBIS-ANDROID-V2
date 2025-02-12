package team.retum.employment.viewmodel

import team.retum.common.base.BaseViewModel
import javax.inject.Inject

class EmploymentViewModel @Inject constructor(
    // TODO : 필요로 하는 유즈케이스(데이터) 작성
) : BaseViewModel<EmploymentState, EmploymentSideEffect>(EmploymentState.getDefaultState()) {

}

data class EmploymentState(
    val employment: Int // TODO : 상태 보완 필요
) {
    companion object {
        fun getDefaultState() = EmploymentState(
            employment = 1
        )
    }
}

sealed interface EmploymentSideEffect { // 익셉션의 집합
    data object BadRequest : EmploymentSideEffect
    data object Success : EmploymentSideEffect
}
