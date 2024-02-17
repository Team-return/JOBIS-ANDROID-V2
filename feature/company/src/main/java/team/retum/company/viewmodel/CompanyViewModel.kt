package team.retum.company.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.exception.BadRequestException
import team.retum.usecase.entity.CompaniesEntity
import team.retum.usecase.usecase.company.FetchCompaniesUseCase
import team.retum.usecase.usecase.company.FetchCompanyCountUseCase
import javax.inject.Inject

@HiltViewModel
internal class CompanyViewModel @Inject constructor(
    private val fetchCompaniesUseCase: FetchCompaniesUseCase,
    private val fetchCompanyCountUseCase: FetchCompanyCountUseCase,
) : BaseViewModel<CompanyState, CompanySideEffect>(CompanyState.getDefaultState()) {

    private var _companies: MutableState<CompaniesEntity> =
        mutableStateOf(CompaniesEntity(emptyList()))
    val companies: MutableState<CompaniesEntity> get() = _companies

    internal fun setName(name: String) {
        _companies = mutableStateOf(CompaniesEntity(emptyList()))
        setState { state.value.copy(name = name, page = 1) }
    }

    internal fun setCheckCompany(check: Boolean) =
        setState { state.value.copy(checkCompany = check) }

    internal fun fetchCompanies(page: Int, name: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCompaniesUseCase.invoke(page = page, name = name).onSuccess {
                val currentCompanies = _companies.value.companies.toMutableList()
                currentCompanies.addAll(it.companies)
                val newCompaniesEntity = _companies.value.copy(companies = currentCompanies)
                _companies.value = newCompaniesEntity
                setState { state.value.copy(page = page + 1) }
            }.onFailure {
                when (it) {
                    is BadRequestException -> {
                        postSideEffect(CompanySideEffect.BadRequest)
                    }
                }
            }
        }
    }

    internal fun fetchTotalCompanyCount(name: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCompanyCountUseCase.invoke(name = name).onSuccess {
                setState { state.value.copy(totalPage = it.totalPageCount) }
            }
        }
    }
}

internal data class CompanyState(
    val name: String?,
    val totalPage: Long,
    val checkCompany: Boolean,
    val page: Int,
) {
    companion object {
        fun getDefaultState() = CompanyState(
            name = null,
            totalPage = 0,
            checkCompany = true,
            page = 1,
        )
    }
}

internal sealed interface CompanySideEffect {
    data object BadRequest : CompanySideEffect
}
