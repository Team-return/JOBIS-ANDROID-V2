package team.retum.bookmark.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import team.retum.bookmark.R
import team.retum.bookmark.viewmodel.BookmarkViewModel
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.usecase.entity.BookmarksEntity

private const val URL = "https://jobis-store.s3.ap-northeast-2.amazonaws.com/"

@Composable
internal fun Bookmarks(
    bookmarkViewModel: BookmarkViewModel = hiltViewModel(),
    onRecruitmentsClick: () -> Unit,
) {
    BookmarkScreen(
        bookmarks = bookmarkViewModel.bookmarks.value.bookmarks,
        onDeleteClick = bookmarkViewModel::bookmarkRecruitment,
        onRecruitmentsClick = onRecruitmentsClick,
    )
}

@Composable
private fun BookmarkScreen(
    bookmarks: List<BookmarksEntity.BookmarkEntity>,
    onDeleteClick: (Long) -> Unit,
    onRecruitmentsClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisLargeTopAppBar(
            title = stringResource(id = R.string.bookmark),
            onBackPressed = null,
        )
        if (bookmarks.isEmpty()) {
            EmptyBookmarkContent(onRecruitmentsClick = onRecruitmentsClick)
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .background(JobisTheme.colors.background),
            ) {
                items(bookmarks) {
                    BookmarkItem(
                        companyName = it.companyName,
                        companyImageUrl = URL + it.companyLogoUrl,
                        date = it.createdAt.substring(0..9),
                        recruitmentId = it.recruitmentId,
                        onDeleteClick = onDeleteClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyBookmarkContent(onRecruitmentsClick: () -> Unit) {
    Column(
        modifier = Modifier
            .background(JobisTheme.colors.background)
            .fillMaxWidth()
            .padding(vertical = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.size(128.dp),
            painter = painterResource(id = R.drawable.ic_empty_bookmark),
            contentDescription = "empty bookmark",
        )
        Spacer(modifier = Modifier.height(16.dp))
        JobisText(
            text = stringResource(id = R.string.empty_bookmark),
            style = JobisTypography.HeadLine,
        )
        Spacer(modifier = Modifier.height(8.dp))
        JobisText(
            text = stringResource(id = R.string.empty_bookmark_description),
            style = JobisTypography.Body,
            color = JobisTheme.colors.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        JobisButton(
            text = stringResource(id = R.string.watch_recruit),
            onClick = onRecruitmentsClick,
            color = ButtonColor.Default,
        )
    }
}

@Composable
private fun BookmarkItem(
    modifier: Modifier = Modifier,
    companyImageUrl: String,
    companyName: String,
    recruitmentId: Long,
    date: String,
    onDeleteClick: (Long) -> Unit,
) {
    Row(
        modifier = modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = companyImageUrl,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            contentDescription = "company image",
        )
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            JobisText(
                text = companyName,
                style = JobisTypography.SubHeadLine,
            )
            JobisText(
                text = date,
                style = JobisTypography.Description,
                color = JobisTheme.colors.inverseOnSurface,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.ic_delete),
            contentDescription = "delete",
            modifier = Modifier.clickable { onDeleteClick(recruitmentId) },
        )
    }
}
