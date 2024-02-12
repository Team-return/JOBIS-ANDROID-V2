package team.retum.usecase.usecase.user

import team.retum.data.repository.UserRepository
import team.retum.network.model.request.SignInRequest
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ) = runCatching {
        userRepository.signIn(
            signInRequest = SignInRequest(
                email = email,
                password = password,
            ),
        )
    }
}
