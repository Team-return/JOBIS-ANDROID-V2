package team.retum.bookmark.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import team.retum.bookmark.R
import team.returm.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.returm.jobisdesignsystemv2.button.ButtonColor
import team.returm.jobisdesignsystemv2.button.JobisButton
import team.returm.jobisdesignsystemv2.foundation.JobisIcon
import team.returm.jobisdesignsystemv2.foundation.JobisTheme
import team.returm.jobisdesignsystemv2.foundation.JobisTypography

// TODO 서버 연동 시 제거
data class BookmarkData(
    val profileImageUrl: String,
    val companyName: String,
    val date: String,
)

@Composable
fun Bookmark() {
    val bookmarkList = emptyList<BookmarkData>()
    BookmarkScreen(bookmarkList = bookmarkList)
}

@Composable
fun BookmarkScreen(bookmarkList: List<BookmarkData>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background)
            .padding(horizontal = 24.dp),
    ) {
        JobisLargeTopAppBar(
            title = stringResource(id = R.string.bookmark),
            onBackPressed = null
        )
        if (bookmarkList.isEmpty()) {
            EmptyBookmarkContent()
        } else {
            LazyColumn(modifier = Modifier.background(JobisTheme.colors.background)) {
                items(bookmarkList) {
                    BookmarkItem(
                        companyName = it.companyName,
                        profileImageUrl = it.profileImageUrl,
                        date = it.date,
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyBookmarkContent() {
    Column(
        modifier = Modifier
            .background(JobisTheme.colors.background)
            .fillMaxWidth()
            .padding(vertical = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(128.dp),
            painter = painterResource(id = R.drawable.ic_empty_bookmark),
            contentDescription = "empty bookmark",
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.empty_bookmark),
            style = JobisTypography.HeadLine,
            color = JobisTheme.colors.onBackground,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.empty_bookmark_description),
            style = JobisTypography.Body,
            color = JobisTheme.colors.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        JobisButton(
            text = stringResource(id = R.string.watch_recruit),
            onClick = { },
            color = ButtonColor.Default,
        )
    }
}

@Composable
fun BookmarkItem(
    modifier: Modifier = Modifier,
    profileImageUrl: String,
    companyName: String,
    date: String,
) {
    Row(
        modifier = modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // TODO AsyncImage 사용
        Image(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            painter = painterResource(id = JobisIcon.Information),
            contentDescription = "company image",
        )
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = companyName,
                style = JobisTypography.SubHeadLine,
                color = JobisTheme.colors.onBackground,
            )
            Text(
                text = date,
                style = JobisTypography.Description,
                color = JobisTheme.colors.inverseOnSurface,
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_delete),
            contentDescription = "delete",
            modifier = Modifier.fillMaxWidth(),
            alignment = Alignment.CenterEnd
        )
    }
}