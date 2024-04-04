package team.retum.company.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.entity.CompaniesEntity
import team.retum.usecase.usecase.company.FetchCompaniesUseCase
import team.retum.usecase.usecase.company.FetchCompanyCountUseCase
import javax.inject.Inject

private const val SEARCH_DEBOUNCE_MILLIS = 1000L

@HiltViewModel
internal class SearchCompaniesViewModel @Inject constructor(
    private val fetchCompaniesUseCase: FetchCompaniesUseCase,
    private val fetchCompanyCountUseCase: FetchCompanyCountUseCase,
) : BaseViewModel<SearchCompaniesState, SearchCompaniesSideEffect>(SearchCompaniesState.getInitialState()) {

    private val _companies: SnapshotStateList<CompaniesEntity.CompanyEntity> = mutableStateListOf()
    internal val companies: List<CompaniesEntity.CompanyEntity> = _companies

    init {
        debounceName()
    }

    @OptIn(FlowPreview::class)
    private fun debounceName() {
        viewModelScope.launch {
            state.map { it.name }.distinctUntilChanged().debounce(SEARCH_DEBOUNCE_MILLIS).collect {
                setState { state.value.copy(showCompaniesEmptyContent = it?.isEmpty() ?: false) }
                if (!it.isNullOrBlank()) {
                    fetchTotalCompanyPageCount()
                    fetchCompanies()
                }
            }
        }
    }

    internal fun setName(name: String) {
        val initialState = SearchCompaniesState.getInitialState()
        _companies.clear()
        setState {
            state.value.copy(
                name = name,
                page = initialState.page,
                totalPage = initialState.totalPage,
            )
        }
    }

    internal fun fetchCompanies() {
        viewModelScope.launch(Dispatchers.IO) {
            with(state.value) {
                fetchCompaniesUseCase(
                    page = page,
                    name = name,
                )
            }.onSuccess {
                setState { state.value.copy(showCompaniesEmptyContent = it.companies.isEmpty()) }
                addPage()
                _companies.addAll(it.companies)
            }.onFailure {
                postSideEffect(SearchCompaniesSideEffect.FetchCompaniesError)
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

internal data class SearchCompaniesState(
    val name: String?,
    val page: Int,
    val totalPage: Long,
    val showCompaniesEmptyContent: Boolean,
) {
    companion object {
        fun getInitialState() = SearchCompaniesState(
            name = null,
            page = 1,
            totalPage = 0,
            showCompaniesEmptyContent = false,
        )
    }
}

internal sealed interface SearchCompaniesSideEffect {
    data object FetchCompaniesError : SearchCompaniesSideEffect
}
