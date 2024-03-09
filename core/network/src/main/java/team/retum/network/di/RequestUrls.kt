package team.retum.network.di

internal object RequestUrls {
    data object PATH {
        const val recruitmentId = "recruitment-id"
        const val companyId = "company-id"
        const val applicationId = "application-id"
        const val reviewId = "review-id"
        const val notificationId = "notification-id"
        const val noticeId = "notice-id"
    }

    data object Users {
        private const val path = "/users"

        const val login = "$path/login"
    }

    data object Auth {
        private const val path = "/auth"

        const val reissue = "$path/reissue"
        const val code = "$path/code"
    }

    data object Recruitments {
        private const val path = "/recruitments"

        const val student = "$path/student"
        const val count = "$path/student/count"
        const val details = "$path/{${PATH.recruitmentId}}"
    }

    data object Companies {
        private const val path = "/companies"

        const val student = "$path/student"
        const val count = "$path/student/count"
        const val review = "$path/review"
        const val details = "$path/{${PATH.companyId}}"
    }

    data object Students {
        private const val path = "/students"

        const val my = "$path/my"
        const val forgottenPassword = "$path/forgotten_password"
        const val signUp = path
        const val exists = "$path/exists"
        const val profile = "$path/profile"
        const val password = "$path/password"
    }

    data object Codes {
        private const val path = "/codes"

        const val codes = path
    }

    data object Applications {
        private const val path = "/applications"

        const val student = "$path/students"
        const val cancel = "$path/{${PATH.applicationId}}"
        const val apply = "$path/{${PATH.recruitmentId}}"
        const val rejection = "$path/rejection/{${PATH.applicationId}}"
        const val reApply = "$path/{${PATH.applicationId}}"
        const val employmentCount = "$path/employment/count"
    }

    data object Files {
        private const val path = "/files"

        const val delete = path
        const val post = path
        const val presignedUrl = "$path/pre-signed"
    }

    data object Reviews {
        private const val path = "/reviews"

        const val details = "$path/details/{${PATH.reviewId}}"
        const val reviews = "$path/{${PATH.companyId}}"
        const val post = path
    }

    data object Bookmarks {
        private const val path = "/bookmarks"

        const val bookmarks = path
        const val bookmark = "$path/{${PATH.recruitmentId}}"
    }

    data object Bugs {
        private const val path = "/bugs"

        const val post = path
    }

    data object Banner {
        private const val path = "/banners"

        const val banners = path
    }

    data object Notification {
        private const val path = "/notifications"

        const val notifications = path
        const val notification = "$path/{${PATH.notificationId}}"
    }

    data object Notice {
        private const val path = "/notices"

        const val notices = path
        const val notice = "$path/{${PATH.noticeId}}"
    }
}
