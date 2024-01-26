package team.retum.network.di

internal sealed class RequestUrls(protected val path: String) {
    data object PATH {
        const val recruitmentId = "recruitment-id"
        const val companyId = "company-id"
        const val applicationId = "application-id"
        const val reviewId = "review-id"
    }

    data object Users : RequestUrls(path = "/users") {
        val login = "$path/login"
    }

    data object Auth : RequestUrls(path = "/auth") {
        val reissue = "$path/reissue"
        val code = "$path/code"
    }

    data object Recruitments : RequestUrls(path = "/recruitments") {
        val student = "$path/student"
        val count = "$path/student/count"
        val details = "$path/{${PATH.recruitmentId}}"
    }

    data object Companies : RequestUrls(path = "/companies") {
        val student = "$path/student"
        val count = "$path/student/count"
        val review = "$path/review"
        val details = "$path/{${PATH.companyId}}"
    }

    data object Students : RequestUrls(path = "/students") {
        val my = "$path/my"
        val forgottenPassword = "$path/forgotten_password"
        val signUp = path
        val exists = "$path/exists"
        val profile = "$path/profile"
        val password = "$path/password"
        val comparePassword = "$path/password"
    }

    data object Codes : RequestUrls(path = "/codes") {
        val codes = path
    }

    data object Applications : RequestUrls(path = "/applications") {
        val student = "$path/students"
        val cancel = "$path/{${PATH.applicationId}}"
        val apply = "$path/{${PATH.recruitmentId}}"
        val rejection = "$path/rejection/{${PATH.applicationId}}"
        val reApply = "$path/{${PATH.applicationId}}"
    }

    data object Files : RequestUrls(path = "/files") {
        val delete = path
        val post = path
        val presignedUrl = "$path/pre-signed"
    }

    data object Reviews : RequestUrls(path = "/reviews") {
        val details = "$path/details/{${PATH.reviewId}}"
        val reviews = "$path/{${PATH.companyId}}"
        val post = path
    }

    data object Bookmarks : RequestUrls(path = "/bookmarks") {
        val bookmarks = path
        val bookmark = "$path/{${PATH.recruitmentId}}"
    }

    data object Bugs : RequestUrls(path = "/bugs") {
        val post = path
    }
}
