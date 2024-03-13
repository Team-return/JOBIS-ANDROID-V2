package team.retum.usecase.entity.company

import team.retum.common.utils.ResourceKeys
import team.retum.network.model.response.company.FetchCompanyDetailsResponse
import java.text.DecimalFormat

data class CompanyDetailsEntity(
    val businessNumber: String,
    val companyName: String,
    val companyProfileUrl: String,
    val companyIntroduce: String,
    val mainAddress: String?,
    val subAddress: String?,
    val managerName: String?,
    val managerPhoneNo: String?,
    val subManagerName: String?,
    val subManagerPhoneNo: String?,
    val fax: String?,
    val email: String?,
    val representativeName: String?,
    val foundedAt: String,
    val workerNumber: String,
    val take: String,
    val recruitmentId: Long?,
    val attachments: List<String>?,
    val serviceName: String?,
    val businessArea: String?,
)

fun FetchCompanyDetailsResponse.toEntity() = CompanyDetailsEntity(
    businessNumber = this.businessNumber,
    companyName = this.companyName,
    companyProfileUrl = ResourceKeys.IMAGE_URL + this.companyProfileUrl,
    companyIntroduce = this.companyIntroduce,
    mainAddress = "${this.mainAddress} ${this.mainAddressDetail}",
    subAddress = this.subAddress?.apply { plus(this@toEntity.subAddressDetail) },
    managerName = this.managerName,
    managerPhoneNo = this.managerPhoneNo?.toPhoneNumber(),
    subManagerName = this.subManagerName,
    subManagerPhoneNo = this.subManagerPhoneNo?.toPhoneNumber(),
    fax = this.fax,
    email = this.email,
    representativeName = this.representativeName,
    foundedAt = this.foundedAt,
    workerNumber = this.workerNumber.toString() + "명",
    take = this.take.toTake(),
    recruitmentId = this.recruitmentId,
    attachments = this.attachments,
    serviceName = this.serviceName,
    businessArea = this.businessArea,
)

private fun Double.toTake(): String {
    val decimalFormat = DecimalFormat("#.##")
    return "${decimalFormat.format(this)}억"
}

private fun String.toPhoneNumber(): String {
    return when (this.length) {
        9 -> {
            this.substring(0..1)
                .plus("-")
                .plus(substring(3..5))
                .plus("-")
                .plus(substring(6..9))
        }

        else -> this.substring(0..2)
            .plus("-")
            .plus(substring(3..6))
            .plus("-")
            .plus(substring(7..10))
    }
}
