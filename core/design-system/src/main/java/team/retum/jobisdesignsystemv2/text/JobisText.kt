package team.retum.jobisdesignsystemv2.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import team.retum.jobisdesignsystemv2.foundation.JobisTheme

/**
 * This composable function creates a JobisText element for use in Jobis.
 *
 * @param modifier The modifier to be applied to the JobisText.
 * @param color The color to be applied to the text.
 * @param text The text to be displayed.
 * @param style The style to be applied to the text.
 * @param overflow How visual overflow should be handled.
 * @param maxLines An optional maximum number of lines
 * @param textDecoration The decorations to paint on the text (e.g., an underline).
 */
@Composable
fun JobisText(
    modifier: Modifier = Modifier,
    color: Color = JobisTheme.colors.onBackground,
    text: String,
    style: TextStyle,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration = TextDecoration.None,
) {
    Text(
        modifier = modifier,
        color = color,
        text = text,
        style = style,
        overflow = overflow,
        maxLines = maxLines,
        textAlign = textAlign,
        textDecoration = textDecoration,
    )
}
