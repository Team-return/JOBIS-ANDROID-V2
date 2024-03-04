package team.retum.jobisdesignsystemv2

import androidx.compose.foundation.clickable
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText

private const val TEXT_TEST_TAG = "TextTest"

/**
 * Verifies that text appears on the display and that styles are applied correctly
 * @see JobisText
 */
@RunWith(AndroidJUnit4::class)
class JobisTextTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun jobisTextTest() {
        composeRule.setContent {
            var style = JobisTypography.Caption
            val clickStyle = JobisTypography.Body
            JobisText(
                text = TEXT_TEST_TAG,
                style = style,
                modifier = Modifier.clickable { style = clickStyle },
            )

            val test = composeRule.onNodeWithText(TEXT_TEST_TAG).assertIsDisplayed()
            test.performClick()

            composeRule.runOnIdle { style = clickStyle }
        }
    }
}
