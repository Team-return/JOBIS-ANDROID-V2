package team.retum.jobisdesignsystemv2.utils

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter

internal const val DURATION_MILLIS = 200
internal const val DEFAULT_PRESS_DEPTH = 0.98f
internal const val MIN_PRESS_DEPTH = 1f
internal const val DEFAULT_DISABLED_MILLIS = 300L

/**
 * This is the ripple upon click used by Jobis.
 *
 * @param enabled Controls the enabled state.
 * @param pressDepth Width of view when pressed
 * @param onPressed Manage the state of the press
 * @param onClick Called when this button is clicked.
 * @param disabledMillis The duration in milliseconds after which the clickable will be re-enabled.
 * @return Modifier that changed ripple on click
 */
@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("ComposableNaming")
@Composable
fun Modifier.clickable(
    enabled: Boolean = true,
    pressDepth: Float = DEFAULT_PRESS_DEPTH,
    onPressed: ((pressed: Boolean) -> Unit)? = null,
    onClick: () -> Unit,
    disabledMillis: Long = DEFAULT_DISABLED_MILLIS,
): Modifier {
    var pressed by remember { mutableStateOf(false) }
    var lastClick by remember { mutableLongStateOf(0L) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) {
            pressDepth
        } else {
            1f
        },
        animationSpec = tween(durationMillis = DURATION_MILLIS),
        label = "",
    )

    return this then Modifier
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .pointerInteropFilter { event ->
            if (enabled) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        pressed = true
                        onPressed?.invoke(true)
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        pressed = false
                        onPressed?.invoke(false)
                        if (event.action == MotionEvent.ACTION_UP && System.currentTimeMillis() - lastClick >= disabledMillis) {
                            lastClick = System.currentTimeMillis()
                            onClick()
                        }
                    }
                }
            }
            true
        }
}
