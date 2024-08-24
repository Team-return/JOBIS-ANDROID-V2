package team.retum.usecase.entity.company

import team.retum.common.utils.ResourceKeys
import team.retum.network.model.response.company.FetchCompanyDetailsResponse
import java.text.DecimalFormat

private const val PHONE_NUMBER_LENGTH_9 = 9
private const val PHONE_NUMBER_LENGTH_10 = 10
private const val PHONE_NUMBER_LENGTH_11 = 11
private const val LOCAL_NUMBER_SEOUL = "02"

private const val LOCAL_NUMBER_START_INDEX = 0
private const val LOCAL_NUMBER_END_INDEX_SEOUL = 1
private const val LOCAL_NUMBER_END_INDEX_OTHERS = 2

private const val MIDDLE_NUMBER_START_INDEX_SEOUL = 2
private const val MIDDLE_NUMBER_START_INDEX_OTHERS = 3

private const val MIDDLE_NUMBER_END_INDEX_SEOUL_4 = 4
private const val MIDDLE_NUMBER_END_INDEX_SEOUL_5 = 5
private const val MIDDLE_NUMBER_END_INDEX_OTHERS_5 = 5
private const val MIDDLE_NUMBER_END_INDEX_OTHERS_6 = 6

private const val END_NUMBER_START_INDEX_SEOUL_5 = 5
private const val END_NUMBER_START_INDEX_SEOUL_6 = 6
private const val END_NUMBER_START_INDEX_OTHERS_6 = 6
private const val END_NUMBER_START_INDEX_OTHERS_7 = 7

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
        PHONE_NUMBER_LENGTH_9 -> {
            substring(LOCAL_NUMBER_START_INDEX..LOCAL_NUMBER_END_INDEX_SEOUL)
                .plus("-")
                .plus(substring(MIDDLE_NUMBER_START_INDEX_SEOUL..MIDDLE_NUMBER_END_INDEX_SEOUL_4))
                .plus("-")
                .plus(substring(END_NUMBER_START_INDEX_SEOUL_5..lastIndex))
        }

        PHONE_NUMBER_LENGTH_10 -> {
            if (isSeoulPhoneNumber()) {
                substring(LOCAL_NUMBER_START_INDEX..LOCAL_NUMBER_END_INDEX_SEOUL)
                    .plus("-")
                    .plus(this.substring(MIDDLE_NUMBER_START_INDEX_SEOUL..MIDDLE_NUMBER_END_INDEX_SEOUL_5))
                    .plus("-")
                    .plus(this.substring(END_NUMBER_START_INDEX_OTHERS_6..lastIndex))
            } else {
                substring(LOCAL_NUMBER_START_INDEX..LOCAL_NUMBER_END_INDEX_OTHERS)
                    .plus("-")
                    .plus(this.substring(MIDDLE_NUMBER_START_INDEX_OTHERS..MIDDLE_NUMBER_END_INDEX_OTHERS_5))
                    .plus("-")
                    .plus(this.substring(END_NUMBER_START_INDEX_SEOUL_6..lastIndex))
            }
        }

        PHONE_NUMBER_LENGTH_11 -> {
            substring(LOCAL_NUMBER_START_INDEX..LOCAL_NUMBER_END_INDEX_OTHERS)
                .plus("-")
                .plus(this.substring(MIDDLE_NUMBER_START_INDEX_OTHERS..MIDDLE_NUMBER_END_INDEX_OTHERS_6))
                .plus("-")
                .plus(this.substring(END_NUMBER_START_INDEX_OTHERS_7..lastIndex))
        }

        else -> {
            this
        }
    }
}

private fun String.isSeoulPhoneNumber(): Boolean {
    val localNumber = substring(LOCAL_NUMBER_START_INDEX..LOCAL_NUMBER_END_INDEX_SEOUL)
    return localNumber == LOCAL_NUMBER_SEOUL
}
