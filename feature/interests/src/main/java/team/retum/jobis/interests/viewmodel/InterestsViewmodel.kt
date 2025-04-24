package team.retum.jobis.interests.viewmodel

import team.retum.common.base.BaseViewModel

internal class InterestsViewmodel constructor(

) : BaseViewModel<InterestsDetailState, InterestSideEffect>(InterestsDetailState.getDefaultState()) {
}

internal data class InterestsDetailState(
    val e: String,
) {
    companion object {
        fun getDefaultState() = InterestsDetailState(
            e = "",
        )
    }
}

internal sealed interface InterestSideEffect
