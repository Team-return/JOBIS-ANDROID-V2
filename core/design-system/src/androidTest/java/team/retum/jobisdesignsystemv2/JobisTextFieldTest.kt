package team.retum.jobisdesignsystemv2

import androidx.compose.foundation.clickable
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
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import team.retum.jobisdesignsystemv2.textfield.DescriptionType
import team.retum.jobisdesignsystemv2.textfield.JobisTextField

private const val TEXT_FIELD_TEST_TAG = "TextTest"

/**
 * Verify that the DescriptionType of the textfield is properly changed and hint and title
 * @see JobisTextField
 * @see DescriptionType
 */
@RunWith(AndroidJUnit4::class)
class JobisTextFieldTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun jobisTextFieldTest() {
        val hint = "hint"
        val checkDescription = "check"
        val errorDescription = "error"
        val informationDescription = "information"
        var type: DescriptionType = DescriptionType.Information
        composeRule.setContent {
            var text by remember { mutableStateOf("") }
            LaunchedEffect(key1 = text) {
                type = when (text.length) {
                    in 0 until 8 -> DescriptionType.Error
                    else -> DescriptionType.Check
                }
            }
            JobisTextField(
                title = TEXT_FIELD_TEST_TAG,
                value = { text },
                hint = hint,
                onValueChange = { },
                checkDescription = checkDescription,
                errorDescription = errorDescription,
                informationDescription = informationDescription,
                descriptionType = type,
                showDescription = { true },
                modifier = Modifier.clickable { text = "text" },
            )
        }

        val titleTest = composeRule.onNodeWithText(TEXT_FIELD_TEST_TAG)
        titleTest.assertIsDisplayed()

        val hintTest = composeRule.onNodeWithText(hint)
        hintTest.assertIsDisplayed()

        val descriptionTest = composeRule.onNodeWithText(TEXT_FIELD_TEST_TAG)
        descriptionTest.performClick()

        composeRule.runOnIdle { assert(type == DescriptionType.Error) }
    }
}
