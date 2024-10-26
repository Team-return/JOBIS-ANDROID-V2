package team.retum.network.model.response.company

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchCompanyDetailsResponse(
    @Json(name = "business_number") val businessNumber: String,
    @Json(name = "company_name") val companyName: String,
    @Json(name = "company_profile_url") val companyProfileUrl: String,
    @Json(name = "company_introduce") val companyIntroduce: String,
    @Json(name = "main_zip_code") val mainZipCode: String?,
    @Json(name = "main_address") val mainAddress: String?,
    @Json(name = "main_address_detail") val mainAddressDetail: String?,
    @Json(name = "manager_name") val managerName: String?,
    @Json(name = "representative_phone_no") val representativePhoneNo: String?,
    @Json(name = "email") val email: String?,
    @Json(name = "representative_name") val representativeName: String?,
    @Json(name = "founded_at") val foundedAt: String,
    @Json(name = "worker_number") val workerNumber: Long,
    @Json(name = "take") val take: Double,
    @Json(name = "recruitment_id") val recruitmentId: Long?,
    @Json(name = "attachments") val attachments: List<String>?,
    @Json(name = "service_name") val serviceName: String?,
    @Json(name = "business_area") val businessArea: String?,
    @Json(name = "headquarter") val headquarter: Boolean
)
