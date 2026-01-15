package i18n

val EN = Model(
    lang = "en",
    question = """
Question:
What is the full model name of your device?

To answer, reply in this way:
/join [device name]
(Example: /join Xiaomi Redmi Note 7)

And the join request will be evaluated automatically!
""".trimIndent(),
    correct = "Welcome! Your request has been approved.",
    incorrect = "Sorry! You are not eligible to join the group.",
    usage = "Usage: /join [device name]",
    notFound = "Join request not found, please try again.",
    timeout = "⏱ Your join request has expired due to no response.\n" +
            "Please send a new join request and answer the question again."
)