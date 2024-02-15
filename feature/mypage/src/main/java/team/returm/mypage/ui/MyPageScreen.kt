package team.returm.mypage.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import team.retum.jobis.mypage.R
import team.returm.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.returm.jobisdesignsystemv2.card.JobisCard
import team.returm.jobisdesignsystemv2.foundation.JobisIcon
import team.returm.jobisdesignsystemv2.foundation.JobisTheme
import team.returm.jobisdesignsystemv2.foundation.JobisTypography
import team.returm.jobisdesignsystemv2.text.JobisText

@Composable
internal fun MyPage() {
    MyPageScreen()
}

@Composable
private fun MyPageScreen() {
    val scrollState = rememberScrollState()
    val (showLogoutModal, setShowLogoutModal) = remember { mutableStateOf(false) }
    val (showWithdrawalModal, setShowWithdrawalModal) = remember { mutableStateOf(false) }

    val onLogoutButtonClick: () -> Unit = {
        setShowLogoutModal(true)
    }

    val onConfirmLogout: () -> Unit = {
        // TODO 로그아웃 처리 로직 작성
        setShowLogoutModal(false)
    }

    val onWithdrawalButtonClick: () -> Unit = {
        setShowWithdrawalModal(true)
    }

    val onConfirmWithdrawal: () -> Unit = {
        // TODO 회원 탈퇴 처리 로직 작성
        setShowWithdrawalModal(false)
    }

    Column {
        JobisLargeTopAppBar(
            title = stringResource(id = R.string.mypage),
            onBackPressed = null,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(JobisTheme.colors.background)
                .padding(horizontal = 24.dp)
                .verticalScroll(state = scrollState),
        ) {
            StudentInfo(
                modifier = Modifier.padding(vertical = 12.dp),
                profileImageUrl = "",
                number = "3125",
                name = "박시원",
                department = "소프트웨어개발과",
                onClick = { /*TODO 회원정보 수정 페이지로 이동 */ },
            )
            WriteInterviewReview(
                companyName = "㈜마이다스아이티주ㅇㅁㅇㄴㅁㅁ",
                onClick = { /*TODO 면접 후기 작 페이지로 이동 */ })
            ContentListItem(
                contentListTitle = stringResource(id = R.string.help),
                contentItemInfo = ContentItemInfo(
                    items = listOf(
                        ListItemInfo(
                            imageResource = painterResource(id = R.drawable.ic_loud_speaker),
                            description = "notice icon",
                            contentTitle = stringResource(id = R.string.notice),
                            onClick = { /*TODO 공지사항 페이지로 이동 */ },
                            iconColor = null,
                        )
                    )
                )
            )
            ContentListItem(
                contentListTitle = stringResource(id = R.string.account),
                contentItemInfo = ContentItemInfo(
                    items = listOf(
                        ListItemInfo(
                            imageResource = painterResource(id = JobisIcon.Code),
                            description = "interest field icon",
                            contentTitle = stringResource(id = R.string.interest_field),
                            onClick = { /*TODO 관심 분야 선택 페이지로 이동 */ },
                            iconColor = JobisTheme.colors.onPrimary,
                        ),
                        ListItemInfo(
                            imageResource = painterResource(id = JobisIcon.LockReset),
                            description = "password change icon",
                            contentTitle = stringResource(id = R.string.password_change),
                            onClick = { /*TODO 비밀번호 변경 페이지로 이동 */ },
                            iconColor = JobisTheme.colors.onPrimary,
                        ),
                        ListItemInfo(
                            imageResource = painterResource(id = JobisIcon.LogOut),
                            description = "logout icon",
                            contentTitle = stringResource(id = R.string.logout),
                            onClick = onLogoutButtonClick,
                            iconColor = JobisTheme.colors.error,
                        ),
                        ListItemInfo(
                            imageResource = painterResource(id = JobisIcon.PersonRemove),
                            description = "membership withdrawal icon",
                            contentTitle = stringResource(id = R.string.membership_withdrawal),
                            onClick = onWithdrawalButtonClick,
                            iconColor = JobisTheme.colors.error,
                        ),
                    )
                )
            )
            ContentListItem(
                contentListTitle = stringResource(id = R.string.bug_report),
                contentItemInfo = ContentItemInfo(
                    items = listOf(
                        ListItemInfo(
                            imageResource = painterResource(id = JobisIcon.Report),
                            description = "bug report icon",
                            contentTitle = stringResource(id = R.string.to_bug_report),
                            onClick = { /*TODO 버그 제보하기 페이지로 이동 */ },
                            iconColor = JobisTheme.colors.onPrimary,
                        ),
                        ListItemInfo(
                            imageResource = painterResource(id = JobisIcon.Box),
                            description = "bug report box icon",
                            contentTitle = stringResource(id = R.string.bug_report_box),
                            onClick = { /*TODO 버그 제보함 페이지로 이동 */ },
                            iconColor = JobisTheme.colors.onPrimary,
                        ),
                    )
                )
                )
            }
        }
        if (showLogoutModal) {
            ShowModal(
                title = stringResource(id = R.string.logout_modal_title),
                description = stringResource(id = R.string.modal_if_logout),
                confirmText = stringResource(id = R.string.modal_button_logout),
                cancelText = stringResource(id = R.string.modal_button_cancel),
                onConfirm = {
                    onConfirmLogout()
                },
                onCancel = {
                    setShowLogoutModal(false)
                }
            )
        } else if (showWithdrawalModal) {
            ShowModal(
                title = stringResource(id = R.string.withdrawal_modal_title),
                description = stringResource(id = R.string.modal_if_withdrawal),
                confirmText = stringResource(id = R.string.modal_button_withdrawal),
                cancelText = stringResource(id = R.string.modal_button_cancel),
                onConfirm = {
                    onConfirmWithdrawal()
                },
                onCancel = {
                    setShowLogoutModal(false)
                }
            )
        }
    }
}

// TODO 모달 구현
@Composable
private fun ShowModal(
    title: String,
    description: String,
    confirmText: String,
    cancelText: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel
    ) {
        Surface() {
            Column() {}
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
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .size(48.dp),
            painter = painterResource(id = R.drawable.ic_user_profile),
            contentDescription = "user profile image",
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
        )
    }
}

@Composable
private fun WriteInterviewReview(
    modifier: Modifier = Modifier,
    companyName: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .padding(vertical = 12.dp, horizontal = 24.dp)
            .background(color = JobisTheme.colors.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = JobisTheme.colors.inverseSurface,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.ic_meeting_room),
                contentDescription = "meeting room icon"
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    JobisText(
                        text = "$companyName".take(7) + "...",
                        style = JobisTypography.Description,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        color = JobisTheme.colors.inverseOnSurface,
                    )
                    JobisText(
                        text = stringResource(id = R.string.write_interview_review),
                        style = JobisTypography.Description,
                        color = JobisTheme.colors.inverseOnSurface,
                    )
                }
                JobisText(
                    modifier = Modifier.clickable { onClick() },
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
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
    ) {
        ContentListTitle(contentListTitle = contentItemInfo.title)
        contentItemInfo.items.forEach { item ->
            ListItem(
                item = item,
                onItemClick = item.onClick,
            )
        }
        JobisCard(onClick = { /*TODO*/ }) {

        }
    }
}

@Composable
private fun ContentListTitle(contentListTitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        JobisText(
            text = contentListTitle,
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
    }
}

@Composable
private fun ListItem(
    item: ListItemInfo,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .clickable { onItemClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .padding(end = 8.dp)
                .size(28.dp),
            painter = item.imageResource,
            contentDescription = item.description,
        )
        JobisText(
            text = item.contentTitle,
            style = JobisTypography.Body,
        )
    }
}

data class ListItemInfo(
    val imageResource: Painter,
    val description: String,
    val contentTitle: String,
    val onClick: () -> Unit,
)

data class ContentItemInfo(
    val title: String,
    val items: List<ListItemInfo>,
)