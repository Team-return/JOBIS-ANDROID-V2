package team.retum.jobisandroidv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import team.retum.jobisandroidv2.ui.JobisApp
import team.retum.jobisdesignsystemv2.foundation.JobisDesignSystemV2Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            JobisDesignSystemV2Theme {
                JobisApp()
            }
        }
    }
}
