import dev.inmo.tgbotapi.extensions.api.chat.invite_links.approveChatJoinRequest
import dev.inmo.tgbotapi.extensions.api.chat.invite_links.declineChatJoinRequest
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.telegramBotWithBehaviourAndLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onChatJoinRequest
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommandWithArgs
import dev.inmo.tgbotapi.extensions.utils.asCommonUser
import dev.inmo.tgbotapi.extensions.utils.asPrivateChat
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.from
import dev.inmo.tgbotapi.types.ChatIdentifier
import dev.inmo.tgbotapi.types.IdChatIdentifier
import dev.inmo.tgbotapi.types.UserId
import dev.inmo.tgbotapi.utils.PreviewFeature
import dev.inmo.tgbotapi.utils.RiskFeature
import i18n.getModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.Locale

data class JoinRequest(
    val identifier: ChatIdentifier,
    val userId: UserId,
)

@OptIn(PreviewFeature::class, RiskFeature::class)
suspend fun main(vararg args: String) {
    var botToken: String? = null
    var acceptedKeyword: String? = null
    var logGroupId: IdChatIdentifier? = null

    if (args.isEmpty() || args.size % 2 != 0 || args.size < 4) {
        printBotUsage()
        return
    }

    for (i in args.indices) {
        when (args[i]) {
            "--token" -> {
                if (i + 1 < args.size) {
                    botToken = args[i + 1]
                }
            }

            "--keyword" -> {
                if (i + 1 < args.size) {
                    acceptedKeyword = args[i + 1]
                }
            }

            "--log" -> {
                if (i + 1 < args.size) {
                    logGroupId = IdChatIdentifier(args[i + 1].toLong())
                }
            }
        }
    }

    if (botToken == null || acceptedKeyword == null) {
        printBotUsage()
        return
    }

    val map = MaxSizeHashMap<ChatIdentifier, JoinRequest>(128)

    telegramBotWithBehaviourAndLongPolling(botToken, CoroutineScope(Dispatchers.IO)) {
        onChatJoinRequest {
            val userId = it.from.id
            val chatId = it.chat.id
            val mapSize = map.size
            val model = getModel(it.from.asCommonUser()?.ietfLanguageCode?.code)

            bot.sendMessage(userId, model.question)
            map[userId] = JoinRequest(chatId, userId)

            println("User $userId started joining $chatId, map size: $mapSize")
        }

        onCommandWithArgs("help") { it, _ ->
            bot.sendMessage(
                it.chat,
                getModel(it.from?.asCommonUser()?.ietfLanguageCode?.code).usage
            )
        }

        onCommandWithArgs("join") { it, args ->
            val user = it.chat.asPrivateChat()!!
            val model = getModel(it.from?.asCommonUser()?.ietfLanguageCode?.code)

            if (args.size <= 1) {
                bot.sendMessage(user.id, model.usage)
                return@onCommandWithArgs
            }

            if (map.containsKey(user.id)) {
                val req = map[user.id]!!
                val message = it.content.text
                val userName = user.username ?: "User"
                val mention = "[$userName](tg://user?id=${user.id.chatId})"
                val logMessage: String

                if (message.contains(
                        acceptedKeyword.lowercase(Locale.getDefault()),
                        ignoreCase = true
                    )
                ) {
                    bot.approveChatJoinRequest(req.identifier, user.id)
                    bot.sendMessage(it.chat, model.correct)

                    logMessage = "#APPROVE:\n" +
                            "Chat: ${req.identifier}\n" +
                            "User: $mention\n" +
                            "User ID: ${user.id.chatId}\n" +
                            "Response: $message"
                    println("User ${user.id} joined ${req.identifier}")

                    map.remove(user.id)
                } else {
                    bot.declineChatJoinRequest(req.identifier, user.id)
                    bot.sendMessage(user.id, model.incorrect)

                    logMessage = "#REJECT:\n" +
                            "Chat: ${req.identifier}\n" +
                            "User: $mention\n" +
                            "User ID: ${user.id.chatId}\n" +
                            "Response: $message"
                    println("User ${user.id} failed to join ${req.identifier}")
                }

                logGroupId?.let { groupId ->
                    bot.sendMessage(groupId, logMessage)
                }
            } else {
                bot.sendMessage(user.id, model.notFound)
                println("User ${user.id} not found in group")
            }
        }
    }.second.join()
}

private fun printBotUsage() {
    println("Usage: java -jar joinreqbot.jar --token {BotToken} --keyword {AcceptedKeyword} --log {LogGroupId}")
}
