package team.retum.jobisandroidv2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import team.retum.jobisandroidv2.ui.JobisApp
import team.retum.jobisdesignsystemv2.foundation.JobisDesignSystemV2Theme
import team.retum.jobisdesignsystemv2.toast.JobisToast

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var delay = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            BackHandler {
                setBackHandler()
            }

            JobisDesignSystemV2Theme {
                JobisApp()
            }
        }
    }

    private fun setBackHandler() {
        if (System.currentTimeMillis() > delay) {
            delay = System.currentTimeMillis() + 1000
            JobisToast.create(
                context = this,
                message = getString(R.string.close_app)
            ).show()
        } else {
            finish()
        }
    }
}
