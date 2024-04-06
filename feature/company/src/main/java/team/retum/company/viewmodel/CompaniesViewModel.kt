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
private const val NUMBER_OF_ITEM_ON_PAGE = 12
private const val LAST_INDEX_OF_PAGE = 11

@HiltViewModel
internal class CompaniesViewModel @Inject constructor(
    private val fetchCompaniesUseCase: FetchCompaniesUseCase,
    private val fetchCompanyCountUseCase: FetchCompanyCountUseCase,
) : BaseViewModel<CompaniesState, CompaniesSideEffect>(CompaniesState.getDefaultState()) {

    private val _companies: SnapshotStateList<CompaniesEntity.CompanyEntity> = mutableStateListOf()
    internal val companies: List<CompaniesEntity.CompanyEntity> = _companies

    init {
        debounceName()
    }

    @OptIn(FlowPreview::class)
    private fun debounceName() {
        viewModelScope.launch {
            state.map { it.name }.distinctUntilChanged().debounce(SEARCH_DEBOUNCE_MILLIS).collect {
                if (!it.isNullOrBlank()) {
                    fetchTotalCompanyPageCount()
                }
            }
        }
    }

    internal fun setName(name: String) {
        val initialState = CompaniesState.getDefaultState()
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
        addCompanyEntities()
        addPage()
        viewModelScope.launch(Dispatchers.IO) {
            with(state.value) {
                fetchCompaniesUseCase(
                    page = page,
                    name = name,
                ).onSuccess {
                    setState { copy(showCompaniesEmptyContent = it.companies.isEmpty()) }
                    replaceCompany(it.companies)
                    _companies.removeAll(_companies.filter { item -> item.id == 0L })
                }.onFailure {
                    postSideEffect(CompaniesSideEffect.FetchCompaniesError)
                }
            }
        }
    }

    private fun addCompanyEntities() {
        repeat(NUMBER_OF_ITEM_ON_PAGE) {
            _companies.add(CompaniesEntity.CompanyEntity.getDefaultEntity())
        }
    }

    private fun replaceCompany(companies: List<CompaniesEntity.CompanyEntity>) {
        val startIndex = _companies.lastIndex - LAST_INDEX_OF_PAGE
        companies.forEachIndexed { index, companyEntity ->
            _companies[startIndex + index] = companyEntity
        }
    }

    internal fun whetherFetchNextPage(lastVisibleItemIndex: Int): Boolean = with(state.value) {
        return lastVisibleItemIndex == companies.lastIndex && page < totalPage
    }

    private fun addPage() = with(state.value) {
        setState { copy(page = page + 1) }
    }

    internal fun fetchTotalCompanyPageCount() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCompanyCountUseCase(name = state.value.name).onSuccess {
                setState { state.value.copy(totalPage = it.totalPageCount.toInt()) }
                fetchCompanies()
            }
        }
    }
}

internal data class CompaniesState(
    val totalPage: Int,
    val page: Int,
    val name: String?,
    val showCompaniesEmptyContent: Boolean,
) {
    companion object {
        fun getDefaultState() = CompaniesState(
            totalPage = 1,
            page = 0,
            name = null,
            showCompaniesEmptyContent = false,
        )
    }
}

internal sealed interface CompaniesSideEffect {
    data object FetchCompaniesError : CompaniesSideEffect
}
