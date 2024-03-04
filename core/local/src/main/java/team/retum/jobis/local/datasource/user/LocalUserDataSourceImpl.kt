package team.retum.jobis.local.datasource.user

import android.content.SharedPreferences
import androidx.core.content.edit
import team.retum.common.utils.ResourceKeys
import javax.inject.Inject

class LocalUserDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : LocalUserDataSource {
    override suspend fun saveAccessToken(accessToken: String) {
        sharedPreferences.edit {
            this.putString(ResourceKeys.ACCESS_TOKEN, accessToken)
        }
    }

    override suspend fun saveAccessExpiresAt(accessExpiresAt: String) {
        sharedPreferences.edit {
            this.putString(ResourceKeys.ACCESS_EXPIRES_AT, accessExpiresAt)
        }
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        sharedPreferences.edit {
            this.putString(ResourceKeys.REFRESH_TOKEN, refreshToken)
        }
    }

    override suspend fun saveRefreshExpiresAt(refreshExpiresAt: String) {
        sharedPreferences.edit {
            this.putString(ResourceKeys.REFRESH_EXPIRES_AT, refreshExpiresAt)
        }
    }

    override fun getAccessToken(): String {
        return sharedPreferences.getString(ResourceKeys.ACCESS_TOKEN, "")
            ?: throw NullPointerException()
    }

    override fun getAccessExpiresAt(): String {
        return sharedPreferences.getString(ResourceKeys.ACCESS_EXPIRES_AT, "")
            ?: throw NullPointerException()
    }

    override fun getRefreshToken(): String {
        return sharedPreferences.getString(ResourceKeys.REFRESH_TOKEN, "")
            ?: throw NullPointerException()
    }

    override fun getRefreshExpiresAt(): String {
        return sharedPreferences.getString(ResourceKeys.REFRESH_EXPIRES_AT, "")
            ?: throw NullPointerException()
    }

    override fun clearUserInformation() {
        sharedPreferences.edit {
            this@edit.clear()
        }
    }
}
