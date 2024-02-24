package team.retum.network.datasource.auth

import team.retum.network.model.request.auth.SendAuthenticationCodeRequest

interface RemoteAuthDataSource {
    suspend fun sendAuthenticationCode(sendAuthenticationCodeRequest: SendAuthenticationCodeRequest)
}
