package team.retum.usecase.usecase.user

import team.retum.data.repository.user.UserRepository
import team.retum.network.model.request.user.SignInRequest
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        deviceToken: String,
    ) = runCatching {
        userRepository.signIn(
            signInRequest = SignInRequest(
                email = email,
                password = password,
                deviceToken = deviceToken,
            ),
        )
    }
}
