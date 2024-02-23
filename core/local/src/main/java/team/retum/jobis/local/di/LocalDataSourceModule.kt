package team.retum.jobis.local.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import team.retum.jobis.local.datasource.user.LocalUserDataSource
import team.retum.jobis.local.datasource.user.LocalUserDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindLocalUserDataSource(localUserDataSourceImpl: LocalUserDataSourceImpl): LocalUserDataSource
}
