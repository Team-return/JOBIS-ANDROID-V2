package team.retum.common.enums

enum class ApplyStatus(val value: String) {
    REQUESTED("승인 요청"),
    APPROVED("승인"),
    FAILED("탈락"),
    PASS("합격"),
    REJECTED("반려"),
    FIELD_TRAIN("현장실습"),
    SEND("지원 중"),
    ACCEPTANCE("근로계약"),
}
