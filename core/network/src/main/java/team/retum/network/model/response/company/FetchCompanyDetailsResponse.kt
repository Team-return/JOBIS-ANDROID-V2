package team.retum.network.model.response.company

import com.google.gson.annotations.SerializedName

data class FetchCompanyDetailsResponse(
    @SerializedName("business_number") val businessNumber: String,
    @SerializedName("company_name") val companyName: String,
    @SerializedName("company_profile_url") val companyProfileUrl: String,
    @SerializedName("company_introduce") val companyIntroduce: String,
    @SerializedName("main_zip_code") val mainZipCode: String?,
    @SerializedName("main_address") val mainAddress: String?,
    @SerializedName("main_address_detail") val mainAddressDetail: String?,
    @SerializedName("sub_zip_code") val subZipCode: String?,
    @SerializedName("sub_address") val subAddress: String?,
    @SerializedName("sub_address_detail") val subAddressDetail: String?,
    @SerializedName("manager_name") val managerName: String?,
    @SerializedName("manager_phone_no") val managerPhoneNo: String?,
    @SerializedName("sub_manager_name") val subManagerName: String?,
    @SerializedName("sub_manager_phone_no") val subManagerPhoneNo: String?,
    @SerializedName("fax") val fax: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("representative_name") val representativeName: String?,
    @SerializedName("founded_at") val foundedAt: String,
    @SerializedName("worker_number") val workerNumber: Long,
    @SerializedName("take") val take: Double,
    @SerializedName("recruitment_id") val recruitmentId: Long?,
    @SerializedName("attachments") val attachments: List<String>?,
    @SerializedName("service_name") val serviceName: String?,
    @SerializedName("business_area") val businessArea: String?
)
