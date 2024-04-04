package team.retum.company.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.entity.CompaniesEntity
import team.retum.usecase.usecase.company.FetchCompaniesUseCase
import team.retum.usecase.usecase.company.FetchCompanyCountUseCase
import javax.inject.Inject

@HiltViewModel
internal class CompaniesViewModel @Inject constructor(
    private val fetchCompaniesUseCase: FetchCompaniesUseCase,
    private val fetchCompanyCountUseCase: FetchCompanyCountUseCase,
) : BaseViewModel<CompaniesState, CompaniesSideEffect>(CompaniesState.getDefaultState()) {

    private val _companies: SnapshotStateList<CompaniesEntity.CompanyEntity> = mutableStateListOf()
    internal val companies: List<CompaniesEntity.CompanyEntity> = _companies

    init {
        fetchTotalCompanyPageCount()
        fetchCompanies()
    }

    internal fun fetchCompanies() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCompaniesUseCase(
                page = state.value.page,
                name = null,
            ).onSuccess {
                addPage()
                _companies.addAll(it.companies)
            }.onFailure {
                postSideEffect(CompaniesSideEffect.FetchCompaniesError)
            }
        }
    }

    internal fun whetherFetchNextPage(lastVisibleItemIndex: Int): Boolean = with(state.value) {
        return lastVisibleItemIndex == companies.lastIndex && page <= totalPage
    }

    private fun addPage() = with(state.value) {
        setState { copy(page = page + 1) }
    }

    private fun fetchTotalCompanyPageCount() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCompanyCountUseCase(name = null).onSuccess {
                setState { state.value.copy(totalPage = it.totalPageCount) }
            }
        }
    }
}

internal data class CompaniesState(
    val totalPage: Long,
    val page: Int,
) {
    companion object {
        fun getDefaultState() = CompaniesState(
            totalPage = 0,
            page = 1,
        )
    }
}

internal sealed interface CompaniesSideEffect {
    data object FetchCompaniesError : CompaniesSideEffect
}
