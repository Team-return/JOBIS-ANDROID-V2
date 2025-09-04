package team.retum.review.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.CodeType
import team.retum.usecase.entity.CodesEntity
import team.retum.usecase.usecase.code.FetchCodeUseCase
import javax.inject.Inject

@HiltViewModel
internal class ReviewsFilterViewModel @Inject constructor(
    private val fetchCodeUseCase: FetchCodeUseCase,
) : BaseViewModel<ReviewsFilterState, Unit>(initialState = ReviewsFilterState.getDefaultState()) {
    private fun fetchCodes() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCodeUseCase(
                keyword = null,
                type = CodeType.JOB,
                parentCode = null,
            ).onSuccess {
                setState {
                    state.value.copy(
                        majorList = it.codes,
                    )
                }
            }
        }
    }
}

@Immutable
data class ReviewsFilterState(
    val majorList: List<CodesEntity.CodeEntity>,
) {
    companion object {
        fun getDefaultState() = ReviewsFilterState(
            majorList = emptyList(),
        )
    }
}

