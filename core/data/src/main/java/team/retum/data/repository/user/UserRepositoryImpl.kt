package team.retum.data.repository.user

import team.retum.jobis.local.datasource.user.LocalUserDataSource
import team.retum.network.datasource.user.RemoteUserDataSource
import team.retum.network.model.request.SignInRequest
import team.retum.network.model.response.TokenResponse
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteUserDataSource: RemoteUserDataSource,
    private val localUserDataSource: LocalUserDataSource,
) : UserRepository {
    override suspend fun signIn(signInRequest: SignInRequest): TokenResponse {
        val response = remoteUserDataSource.signIn(signInRequest = signInRequest)
        localUserDataSource.run {
            saveAccessToken(response.accessToken)
            saveAccessExpiresAt(response.accessExpiresAt)
            saveRefreshToken(response.refreshToken)
            saveRefreshExpiresAt(response.refreshExpiresAt)
        }
        return response
    }

    override suspend fun saveAccessToken(accessToken: String) {
        localUserDataSource.saveAccessToken(accessToken)
    }

    override suspend fun saveAccessExpiresAt(accessExpiresAt: String) {
        localUserDataSource.saveAccessExpiresAt(accessExpiresAt)
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        localUserDataSource.saveRefreshToken(refreshToken)
    }

    override suspend fun saveRefreshExpiresAt(refreshExpiresAt: String) {
        localUserDataSource.saveRefreshExpiresAt(refreshExpiresAt)
    }

    override fun getAccessToken(): String {
        return localUserDataSource.getAccessToken()
    }

    override fun getAccessExpiresAt(): String {
        return localUserDataSource.getAccessExpiresAt()
    }

    override fun getRefreshToken(): String {
        return localUserDataSource.getRefreshToken()
    }

    override fun getRefreshExpiresAt(): String {
        return localUserDataSource.getRefreshExpiresAt()
    }

    override fun signOut() {
        localUserDataSource.clearUserInformation()
    }
}
