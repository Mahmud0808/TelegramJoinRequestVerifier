package i18n

val ZH = Model(
    lang = "zh",
    question = """
问题：
您的设备完整型号是什么？

请按以下方式回答：
/join [设备名称]
（例如：/join Xiaomi Redmi Note 7）

加入请求将自动审核！
""".trimIndent(),
    correct = "欢迎！您的请求已被批准。",
    incorrect = "抱歉！您没有资格加入该群组。",
    usage = "用法：/join [设备名称]",
    notFound = "未找到加入请求，请重试。",
    timeout = "⏱ 由于未响应，您的加入请求已过期。\n" +
            "请发送新的加入请求并再次回答问题。"
)