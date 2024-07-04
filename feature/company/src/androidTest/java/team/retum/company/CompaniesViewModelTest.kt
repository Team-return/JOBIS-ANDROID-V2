package team.retum.company

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import team.retum.company.viewmodel.CompaniesViewModel
import team.retum.usecase.usecase.company.FetchCompaniesUseCase
import team.retum.usecase.usecase.company.FetchCompanyCountUseCase
import javax.inject.Inject

/**
 * This class test Companies view model
 *
 * The view model was set up through hilt’s dependency injection.
 *
 * First setNmae function is used when user search company so this test will check input is setting in state.value.name
 *
 * See [CompaniesViewModel]
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CompaniesViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fetchCompaniesUseCase: FetchCompaniesUseCase

    @Inject
    lateinit var fetchCompanyCountUseCase: FetchCompanyCountUseCase

    private lateinit var viewModel: CompaniesViewModel

    @Before
    fun init() {
        hiltRule.inject()
        viewModel = CompaniesViewModel(fetchCompaniesUseCase, fetchCompanyCountUseCase)
    }

    @Test
    fun setNameTest() {
        val newName = "홍승재 타이어"

        viewModel.setName(newName)

        assertEquals(newName, viewModel.state.value.name)
        assertTrue(viewModel.companies.isEmpty())
    }
}
