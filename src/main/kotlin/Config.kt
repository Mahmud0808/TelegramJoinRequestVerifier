/**
 * Configuration object that reads from environment variables with CLI argument fallback.
 **/
object Config {
    var botToken: String = ""
        private set
    var acceptedKeyword: String = ""
        private set
    var logGroupId: Long? = null
        private set
    var timeoutMinutes: Long = 10 // REQUIRED: 10 minutes
        private set

    fun initialize(args: Array<out String>) {
        botToken = System.getenv("BOT_TOKEN") ?: getArgValue(args, "--token") ?: ""
        acceptedKeyword = System.getenv("ACCEPTED_KEYWORD") ?: getArgValue(args, "--keyword") ?: ""
        logGroupId =
            System.getenv("LOG_GROUP_ID")?.toLongOrNull()
                ?: getArgValue(args, "--log")?.toLongOrNull()

        timeoutMinutes = System.getenv("TIMEOUT_MINUTES")?.toLongOrNull() ?: 10
    }

    private fun getArgValue(args: Array<out String>, key: String): String? {
        val index = args.indexOf(key)
        return if (index != -1 && index + 1 < args.size) args[index + 1] else null
    }

    fun isValid(): Boolean = botToken.isNotBlank() && acceptedKeyword.isNotBlank()

    fun printUsage() {
        println(
            """
            |Usage: java -jar joinreqbot.jar [options]
            |
            |Environment Variables (preferred):
            |  BOT_TOKEN          Required. Telegram bot token
            |  ACCEPTED_KEYWORD   Required. Keyword for approval
            |  LOG_GROUP_ID       Optional. Chat ID for logging
            |  TIMEOUT_MINUTES    Optional. Request timeout in minutes (default: 10)
            |
            |CLI Arguments (fallback):
            |  --token <token>    Bot token
            |  --keyword <word>   Accepted keyword
            |  --log <chat_id>    Log group ID
            """.trimMargin()
        )
    }
}
