package team.retum.common.exception

data object BadRequestException : RuntimeException()
data object UnAuthorizedException : RuntimeException()
data object ForbiddenException : RuntimeException()
data object NotFoundException : RuntimeException()
data object MethodNotAllowedException : RuntimeException()
data object ConflictException : RuntimeException()
data object TooManyRequestException : RuntimeException()
data object ServerException : RuntimeException()
data object OfflineException : RuntimeException()
data object ConnectionTimeOutException : RuntimeException()
data object UnknownException : RuntimeException()
data object ReissueException : RuntimeException()
