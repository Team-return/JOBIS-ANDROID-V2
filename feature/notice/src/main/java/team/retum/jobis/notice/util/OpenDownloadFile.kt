package team.retum.jobis.notice.util

import android.content.Context
import android.content.Intent
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.File
import java.util.Locale

fun openDownloadedFile(
    filePath: String,
    context: Context,
) {
    val file = File(filePath)
    val uri = FileProvider.getUriForFile(
        context,
        context.packageName + ".provider",
        file,
    )
    val intent = Intent(Intent.ACTION_VIEW)
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    intent.setDataAndType(uri, getMimeType(filePath))
    context.startActivity(intent)
}

private fun getMimeType(filePath: String): String? {
    val extension = MimeTypeMap.getFileExtensionFromUrl(filePath)
    return MimeTypeMap.getSingleton()
        .getMimeTypeFromExtension(extension.lowercase(Locale.getDefault()))
}
