package team.retum.signin

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import team.retum.jobisdesignsystemv2.foundation.JobisDesignSystemV2Theme
import team.retum.signin.ui.SignIn
import team.retum.signin.viewmodel.SignInViewModel

private const val DELAY_BUTTON_CLICK = 3000L

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SignInViewModelTest {

    @get:Rule(order = 1)
    val hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<SignInTestActivity>()

    private val emailInput by lazy {
        composeTestRule.onNodeWithTag("이메일")
    }

    private val passwordInput by lazy {
        composeTestRule.onNodeWithTag("비밀번호")
    }

    private val signInButton by lazy {
        composeTestRule.onNodeWithTag("로그인")
    }

    private val signInViewModel by lazy {
        composeTestRule.activity.viewModels<SignInViewModel>().value
    }

    private fun init() {
        hiltTestRule.inject()
        composeTestRule.setContent {
            JobisDesignSystemV2Theme {
                SignIn(
                    onBackClick = { /*TODO*/ },
                    onSignInSuccess = { /*TODO*/ },
                    onForgotPasswordClick = { /*TODO*/ },
                )
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("sign_in")
    }

    @Test
    fun sign_in_inputs_displayed_test() {
        init()
        emailInput.assertIsDisplayed()
        passwordInput.assertIsDisplayed()
        signInButton.assertIsDisplayed()
    }

    @Test
    fun input_value_to_sign_in_inputs_test() {
        init()
        enter_dummy_values()

        emailInput.assertTextContains("abcd1234@@")
        passwordInput.assertTextContains("••••••••••")
    }

    @Test
    fun sign_in_button_enabled_test() {
        init()
        enter_dummy_values()

        signInButton.assertIsEnabled()
        signInButton.performClick()
        signInButton.assertIsNotEnabled()
    }

    @Test
    fun email_invalid_test() {
        init()
        signInViewModel.setEmail(BuildConfig.TEST_EMAIL)
        signInViewModel.setPassword("abcd1234@@")

        signInButton.performClick()
        signInButton.assertIsNotEnabled()

        Thread.sleep(DELAY_BUTTON_CLICK)

        composeTestRule.onNodeWithText("비밀번호가 옳지 않아요").assertIsDisplayed()

        signInViewModel.setPassword("abcd1234@")
        signInButton.assertIsEnabled()
    }

    @Test
    fun password_invalid_test() {
        init()
        signInViewModel.setEmail("abcd1234@@")
        signInViewModel.setPassword(BuildConfig.TEST_PASSWORD)

        signInButton.performClick()
        signInButton.assertIsNotEnabled()

        Thread.sleep(DELAY_BUTTON_CLICK)

        composeTestRule.onNodeWithText("아이디를 찾지 못했어요").assertIsDisplayed()

        signInViewModel.setEmail("abcd1234@")
        signInButton.assertIsEnabled()
    }

    @Test
    fun show_toast_test() {
        init()
        enter_dummy_values()

    }

    private fun enter_dummy_values() {
        signInViewModel.setEmail("abcd1234@@")
        signInViewModel.setPassword("abcd1234@@")
    }
}

@AndroidEntryPoint
class SignInTestActivity : ComponentActivity()
