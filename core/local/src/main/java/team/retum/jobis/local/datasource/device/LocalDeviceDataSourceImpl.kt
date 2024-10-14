package team.retum.jobis.local.datasource.device

import android.content.SharedPreferences
import androidx.core.content.edit
import team.retum.common.utils.ResourceKeys
import team.retum.jobis.local.DeviceDataSource
import javax.inject.Inject

class LocalDeviceDataSourceImpl @Inject constructor(
    @DeviceDataSource private val sharedPreferences: SharedPreferences,
): LocalDeviceDataSource {
    override suspend fun saveDeviceToken(deviceToken: String) {
        sharedPreferences.edit {
            this.putString(ResourceKeys.DEVICE_TOKEN, deviceToken)
        }
    }

    override suspend fun getDeviceToken(): String {
        return sharedPreferences.getString(ResourceKeys.DEVICE_TOKEN, "")
            ?: throw NullPointerException()
    }

    override suspend fun clearDeviceToken() {
        sharedPreferences.edit {
            this@edit.clear()
        }
    }
}
