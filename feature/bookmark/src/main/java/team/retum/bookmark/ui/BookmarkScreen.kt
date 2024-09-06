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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import team.retum.bookmark.R
import team.retum.bookmark.viewmodel.BookmarkSideEffect
import team.retum.bookmark.viewmodel.BookmarkViewModel
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.usecase.entity.BookmarksEntity

@Composable
internal fun Bookmarks(
    onRecruitmentsClick: () -> Unit,
    onRecruitmentDetailClick: (Long) -> Unit,
    bookmarkViewModel: BookmarkViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        with(bookmarkViewModel) {
            clearBookmarks()
            fetchBookmarks()
            sideEffect.collect {
                when (it) {
                    is BookmarkSideEffect.BadRequest -> {
                        JobisToast.create(
                            context = context,
                            message = context.getString(R.string.toast_fetch_bookmark_bad_request),
                            drawable = JobisIcon.Error,
                        ).show()
                    }
                }
            }
        }
    }

    BookmarkScreen(
        bookmarks = bookmarkViewModel.bookmarks.toPersistentList(),
        onDeleteClick = bookmarkViewModel::bookmarkRecruitment,
        onRecruitmentsClick = onRecruitmentsClick,
        onRecruitmentDetailClick = onRecruitmentDetailClick,
    )
}

@Composable
private fun BookmarkScreen(
    bookmarks: ImmutableList<BookmarksEntity.BookmarkEntity>,
    onDeleteClick: (Long) -> Unit,
    onRecruitmentsClick: () -> Unit,
    onRecruitmentDetailClick: (Long) -> Unit,
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
                items(
                    items = bookmarks,
                    key = { it.recruitmentId },
                ) {
                    BookmarkItem(
                        companyName = it.companyName,
                        companyImageUrl = it.companyLogoUrl,
                        date = it.createdAt,
                        recruitmentId = it.recruitmentId,
                        onDeleteClick = onDeleteClick,
                        onRecruitmentDetailClick = onRecruitmentDetailClick,
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
    onRecruitmentDetailClick: (Long) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = modifier
                .weight(1f)
                .padding(vertical = 16.dp)
                .clickable(onClick = { onRecruitmentDetailClick(recruitmentId) }),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = companyImageUrl,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                contentDescription = "company image",
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                JobisText(
                    text = companyName,
                    style = JobisTypography.SubHeadLine,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                JobisText(
                    text = date,
                    style = JobisTypography.Description,
                    color = JobisTheme.colors.inverseOnSurface,
                )
            }
        }
        JobisIconButton(
            drawableResId = JobisIcon.Delete,
            contentDescription = "delete",
            onClick = { onDeleteClick(recruitmentId) },
        )
    }
}
