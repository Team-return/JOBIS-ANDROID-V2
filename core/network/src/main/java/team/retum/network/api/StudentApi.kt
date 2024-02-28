package team.retum.network.api

import retrofit2.http.GET
import retrofit2.http.Query
import team.retum.network.di.RequestUrls
import team.retum.network.model.response.FetchStudentInformationResponse

interface StudentApi {
    @GET(RequestUrls.Students.exists)
    suspend fun checkStudentExists(
        @Query("gcn") gcn: String,
        @Query("name") name: String,
    )

    @GET(RequestUrls.Students.my)
    suspend fun fetchStudentInformation(): FetchStudentInformationResponse
}
