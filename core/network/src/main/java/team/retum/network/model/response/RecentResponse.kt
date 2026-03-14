package team.retum.network.model.response

data class RecentResponse (
    val companies: List<RecentCompanyResponse>,
)

data class RecentCompanyResponse(
    val company_id: Long,
    val company_name: String,
    val company_introduce: String,
    val company_logo_url: String,
)
