package team.retum.jobis.local

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DeviceDataSource
