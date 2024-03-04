package team.returm.mypage.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import team.retum.jobis.mypage.R
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.card.JobisCard
import team.retum.jobisdesignsystemv2.dialog.JobisDialog
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable
import team.returm.mypage.viewmodel.MyPageSideEffect
import team.returm.mypage.viewmodel.MyPageState
import team.returm.mypage.viewmodel.MyPageViewModel

@Composable
internal fun MyPage(
    onSelectInterestClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onReportBugClick: () -> Unit,
    onNoticeClick: () -> Unit,
    navigateToLanding: () -> Unit,
    myPageViewModel: MyPageViewModel = hiltViewModel(),
    onPostReviewClick: () -> Unit,
) {
    val state by myPageViewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        myPageViewModel.sideEffect.collect {
            when (it) {
                is MyPageSideEffect.SuccessSignOut -> {
                    navigateToLanding()
                }
            }
        }
    }

    MyPageScreen(
        onSelectInterestClick = onSelectInterestClick,
        onChangePasswordClick = onChangePasswordClick,
        onReportBugClick = onReportBugClick,
        onNoticeClick = onNoticeClick,
        state = state,
        scrollState = scrollState,
        setShowSignOutModal = myPageViewModel::setShowSignOutModal,
        setShowWithdrawalModal = myPageViewModel::setShowWithdrawalModal,
        onSignOutClick = myPageViewModel::onSignOutClick,
        onWithdrawalClick = myPageViewModel::onWithdrawalClick,
        onPostReviewClick = onPostReviewClick,
    )
}

@Composable
private fun MyPageScreen(
    onSelectInterestClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onReportBugClick: () -> Unit,
    onNoticeClick: () -> Unit,
    state: MyPageState,
    scrollState: ScrollState,
    setShowSignOutModal: (Boolean) -> Unit,
    setShowWithdrawalModal: (Boolean) -> Unit,
    onSignOutClick: () -> Unit,
    onWithdrawalClick: () -> Unit,
    onPostReviewClick: () -> Unit,
) {
    if (state.showSignOutModal) {
        JobisDialog(
            onDismissRequest = { setShowSignOutModal(false) },
            title = stringResource(id = R.string.sign_out_modal_title),
            description = stringResource(id = R.string.modal_if_sign_out),
            subButtonText = stringResource(id = R.string.modal_button_cancel),
            mainButtonText = stringResource(id = R.string.modal_button_logout),
            onSubButtonClick = { setShowSignOutModal(false) },
            onMainButtonClick = onSignOutClick,
        )
    } else if (state.showWithdrawalModal) {
        JobisDialog(
            onDismissRequest = { setShowWithdrawalModal(false) },
            title = stringResource(id = R.string.withdrawal_modal_title),
            description = stringResource(id = R.string.modal_if_withdrawal),
            subButtonText = stringResource(id = R.string.modal_button_cancel),
            mainButtonText = stringResource(id = R.string.modal_button_withdrawal),
            onSubButtonClick = { setShowSignOutModal(false) },
            onMainButtonClick = onWithdrawalClick,
        )
    }

    Column {
        JobisLargeTopAppBar(title = stringResource(id = R.string.mypage))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(JobisTheme.colors.background)
                .padding(horizontal = 24.dp)
                .verticalScroll(state = scrollState),
        ) {
            with(state.studentInformation) {
                StudentInfo(
                    modifier = Modifier.padding(vertical = 12.dp),
                    profileImageUrl = state.studentInformation.profileImageUrl,
                    number = studentGcn,
                    name = studentName,
                    department = department.value,
                    onClick = { /*TODO 회원정보 수정 페이지로 이동 */ },
                )
            }
            state.reviewableCompany?.run {
                WriteInterviewReview(
                    companyName = state.reviewableCompany.name,
                    onClick = { /*TODO 면접 후기 작 페이지로 이동 */ },
                )
            }
            ContentListItem(
                contentListTitle = stringResource(id = R.string.help),
                contentItemInfo = ContentItemInfo(
                    items = listOf(
                        ListItemInfo(
                            imageResource = painterResource(id = R.drawable.ic_loud_speaker),
                            description = "notice icon",
                            contentTitle = stringResource(id = R.string.notice),
                            onClick = onNoticeClick,
                            iconColor = null,
                        ),
                    ),
                ),
            )
            ContentListItem(
                contentListTitle = stringResource(id = R.string.account),
                contentItemInfo = ContentItemInfo(
                    items = listOf(
                        ListItemInfo(
                            imageResource = painterResource(id = JobisIcon.Code),
                            description = "interest field icon",
                            contentTitle = stringResource(id = R.string.interest_field),
                            onClick = onSelectInterestClick,
                            iconColor = JobisTheme.colors.onPrimary,
                        ),
                        ListItemInfo(
                            imageResource = painterResource(id = JobisIcon.LockReset),
                            description = "password change icon",
                            contentTitle = stringResource(id = R.string.password_change),
                            onClick = onChangePasswordClick,
                            iconColor = JobisTheme.colors.onPrimary,
                        ),
                        ListItemInfo(
                            imageResource = painterResource(id = JobisIcon.LogOut),
                            description = "logout icon",
                            contentTitle = stringResource(id = R.string.logout),
                            onClick = { setShowSignOutModal(true) },
                            iconColor = JobisTheme.colors.error,
                        ),
                        ListItemInfo(
                            imageResource = painterResource(id = JobisIcon.PersonRemove),
                            description = "membership withdrawal icon",
                            contentTitle = stringResource(id = R.string.membership_withdrawal),
                            onClick = { setShowWithdrawalModal(true) },
                            iconColor = JobisTheme.colors.error,
                        ),
                    ),
                ),
            )
            ContentListItem(
                contentListTitle = stringResource(id = R.string.bug_report),
                contentItemInfo = ContentItemInfo(
                    items = listOf(
                        ListItemInfo(
                            imageResource = painterResource(id = JobisIcon.Report),
                            description = "bug report icon",
                            contentTitle = stringResource(id = R.string.to_bug_report),
                            onClick = onReportBugClick,
                            iconColor = JobisTheme.colors.onPrimary,
                        ),
                        ListItemInfo(
                            imageResource = painterResource(id = JobisIcon.Box),
                            description = "bug report box icon",
                            contentTitle = stringResource(id = R.string.bug_report_box),
                            onClick = { /*TODO 버그 제보함 페이지로 이동 */ },
                            iconColor = JobisTheme.colors.onPrimary,
                        ),
                    ),
                ),
            )
        }
    }
}

@Composable
private fun StudentInfo(
    modifier: Modifier = Modifier,
    profileImageUrl: String,
    number: String,
    name: String,
    department: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            model = profileImageUrl,
            contentDescription = "user profile image",
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            JobisText(
                text = "$number $name",
                style = JobisTypography.SubHeadLine,
            )
            JobisText(
                text = department,
                style = JobisTypography.Description,
                color = JobisTheme.colors.inverseOnSurface,
            )
        }
        JobisText(
            text = stringResource(id = R.string.modify),
            style = JobisTypography.SubHeadLine,
            color = JobisTheme.colors.onPrimary,
            modifier = Modifier.clickable(onClick = onClick),
        )
    }
}

@Composable
private fun WriteInterviewReview(
    modifier: Modifier = Modifier,
    companyName: String,
    onClick: () -> Unit,
) {
    JobisCard(
        modifier = modifier.padding(vertical = 12.dp),
        background = JobisTheme.colors.inverseSurface,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = JobisTheme.colors.inverseSurface)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = JobisIcon.MeetingRoom),
                contentDescription = "meeting room icon",
                tint = JobisTheme.colors.onPrimary,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    JobisText(
                        text = companyName.take(10) + "...",
                        style = JobisTypography.Description,
                        overflow = TextOverflow.Ellipsis,
                        color = JobisTheme.colors.inverseOnSurface,
                        maxLines = 1,
                    )
                    JobisText(
                        text = stringResource(id = R.string.write_interview_review),
                        style = JobisTypography.Description,
                        color = JobisTheme.colors.inverseOnSurface,
                    )
                }
                JobisText(
                    modifier = Modifier.clickable(onClick = onClick),
                    text = stringResource(id = R.string.go_write),
                    style = JobisTypography.SubHeadLine,
                    textAlign = TextAlign.Center,
                    color = JobisTheme.colors.onPrimary,
                )
            }
        }
    }
}

@Composable
private fun ContentListItem(
    contentItemInfo: ContentItemInfo,
    contentListTitle: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
    ) {
        JobisText(
            modifier = Modifier.padding(vertical = 8.dp),
            text = contentListTitle,
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        contentItemInfo.items.forEach { item ->
            ListItem(
                item = item,
                onItemClick = item.onClick,
            )
        }
    }
}

@Composable
private fun ListItem(
    item: ListItemInfo,
    onItemClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable(onClick = onItemClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            modifier = Modifier.size(28.dp),
            painter = item.imageResource,
            contentDescription = item.description,
            tint = item.iconColor ?: Color.Unspecified,
        )
        JobisText(
            text = item.contentTitle,
            style = JobisTypography.Body,
        )
    }
}

private data class ListItemInfo(
    val imageResource: Painter,
    val description: String,
    val contentTitle: String,
    val onClick: () -> Unit,
    val iconColor: Color? = null,
)

private data class ContentItemInfo(
    val items: List<ListItemInfo>,
)
