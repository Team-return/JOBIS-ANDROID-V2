package team.retum.network.api

import retrofit2.http.GET
import retrofit2.http.Query
import team.retum.network.di.RequestUrls

interface StudentApi {
    @GET(RequestUrls.Students.exists)
    suspend fun checkStudentExists(
        @Query("gcn") gcn: String,
        @Query("name") name: String,
    )
}
