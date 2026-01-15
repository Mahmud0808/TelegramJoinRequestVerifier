package i18n

val KO = Model(
    lang = "ko",
    question = """
질문:
사용 중인 기기의 전체 모델 이름은 무엇인가요?

다음과 같이 답변하세요:
/join [기기 이름]
(예: /join Xiaomi Redmi Note 7)

가입 요청은 자동으로 평가됩니다!
""".trimIndent(),
    correct = "환영합니다! 요청이 승인되었습니다.",
    incorrect = "죄송합니다! 그룹에 가입할 자격이 없습니다.",
    usage = "사용법: /join [기기 이름]",
    notFound = "가입 요청을 찾을 수 없습니다. 다시 시도해주세요.",
    timeout = "⏱ 응답이 없어 가입 요청이 만료되었습니다.\n" +
            "새로운 요청을 보내고 다시 질문에 답해주세요."
)