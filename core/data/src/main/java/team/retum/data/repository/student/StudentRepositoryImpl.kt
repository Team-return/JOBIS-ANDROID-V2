package team.retum.data.repository.student

import team.retum.jobis.local.datasource.user.LocalUserDataSource
import team.retum.network.datasource.student.RemoteStudentDataSource
import team.retum.network.model.response.FetchStudentInformationResponse
import team.retum.network.model.request.student.PostSignUpRequest
import javax.inject.Inject

class StudentRepositoryImpl @Inject constructor(
    private val remoteStudentDataSource: RemoteStudentDataSource,
    private val localUserDataSource: LocalUserDataSource,
) : StudentRepository {
    override suspend fun checkStudentExists(
        gcn: String,
        name: String,
    ) {
        remoteStudentDataSource.checkStudentExists(
            gcn = gcn,
            name = name,
        )
    }

    override suspend fun fetchStudentInformation(): FetchStudentInformationResponse {
        return remoteStudentDataSource.fetchStudentInformation()
    }

    override suspend fun postSignUp(postSignUpRequest: PostSignUpRequest) {
        val response = remoteStudentDataSource.postSignUp(postSignUpRequest = postSignUpRequest)
        localUserDataSource.run {
            saveAccessToken(response.accessToken)
            saveAccessExpiresAt(response.accessExpiresAt)
            saveRefreshToken(response.refreshToken)
            saveRefreshExpiresAt(response.refreshExpiresAt)
        }
    }
}
