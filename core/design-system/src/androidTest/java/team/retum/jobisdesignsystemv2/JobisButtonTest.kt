package team.retum.jobisdesignsystemv2

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import team.retum.jobisdesignsystemv2.button.JobisButton

private const val BTN_TEST_TAG = "ButtonTest"

/**
 * Verifies that the button appears on the display and that a click action is performed
 * assertHasClickAction cannot be used because JobisButton does not use the button's onClick
 * @see JobisButton
 * @see assertHasClickAction
 */
@RunWith(AndroidJUnit4::class)
class JobisButtonTest {
    @get:Rule
    val composeRule = createComposeRule()
    private var clicked = 0

    @Test
    fun enabledJobisButtonTest() {
        composeRule.setContent {
            JobisButton(
                modifier = Modifier.testTag(BTN_TEST_TAG),
                text = BTN_TEST_TAG,
                onClick = { clicked++ },
                enabled = false,
            )
        }
        composeRule
            .onNodeWithTag(BTN_TEST_TAG)
            .assertIsDisplayed()
            .performClick()

        composeRule.runOnIdle { assert(clicked == 0) }
    }

    @Test
    fun jobisButtonTest() {
        composeRule.setContent {
            JobisButton(
                modifier = Modifier.testTag(BTN_TEST_TAG),
                text = BTN_TEST_TAG,
                onClick = { clicked++ },
                enabled = true,
            )
        }
        composeRule
            .onNodeWithTag(BTN_TEST_TAG)
            .assertIsDisplayed()
            .performClick()

        composeRule.runOnIdle { assert(clicked == 1) }
    }
}
