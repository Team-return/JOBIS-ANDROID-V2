package team.retum.company.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.entity.company.CompanyDetailsEntity
import team.retum.usecase.usecase.company.FetchCompanyDetailsUseCase
import javax.inject.Inject

@HiltViewModel
internal class CompanyDetailsViewModel @Inject constructor(
    private val fetchCompanyDetailsUseCase: FetchCompanyDetailsUseCase,
) : BaseViewModel<CompanyDetailsState, Unit>(CompanyDetailsState.getInitialState()) {

    internal fun setCompanyId(companyId: Long) = setState {
        state.value.copy(companyId = companyId)
    }

    internal fun fetchCompanyDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCompanyDetailsUseCase(companyId = state.value.companyId).onSuccess {
                setState { state.value.copy(companyDetailsEntity = it) }
            }
        }
    }
}

internal data class CompanyDetailsState(
    val companyId: Long,
    val companyDetailsEntity: CompanyDetailsEntity,
) {
    companion object {
        fun getInitialState() = CompanyDetailsState(
            companyId = 0,
            companyDetailsEntity = CompanyDetailsEntity(
                businessNumber = "",
                companyName = "",
                companyProfileUrl = "",
                companyIntroduce = "",
                mainAddress = "",
                subAddress = "",
                managerName = "",
                managerPhoneNo = "",
                subManagerName = "",
                subManagerPhoneNo = "",
                fax = "",
                email = "",
                representativeName = "",
                foundedAt = "",
                workerNumber = "",
                take = "",
                recruitmentId = 0,
                attachments = emptyList(),
                serviceName = "",
                businessArea = "",
            ),
        )
    }
}
