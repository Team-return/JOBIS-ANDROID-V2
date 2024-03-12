package team.retum.usecase.usecase.student

import team.retum.data.repository.user.UserRepository
import team.retum.network.model.request.user.RegisterDeviceTokenRequest
import javax.inject.Inject

class RegisterDeviceTokenUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(deviceToken: String) = runCatching {
        userRepository.registerDeviceToken(
            registerDeviceTokenRequest = RegisterDeviceTokenRequest(
                deviceToken = deviceToken,
            ),
        )
    }
}
