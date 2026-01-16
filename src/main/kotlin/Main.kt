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
import dev.inmo.tgbotapi.utils.PreviewFeature
import dev.inmo.tgbotapi.utils.RiskFeature
import i18n.getModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(PreviewFeature::class, RiskFeature::class)
suspend fun main(vararg args: String) {
    Runtime.getRuntime().addShutdownHook(Thread {
        println("Bot is stopping... Goodbye 👋")
    })
    println("Started join request verifier bot.")

    Config.initialize(args)

    if (!Config.isValid()) {
        Config.printUsage()
        return
    }

    val timeoutMs = Config.timeoutMinutes * 60_000L
    val requestMap = MaxSizeHashMap<ChatIdentifier, JoinRequest>(128)

    telegramBotWithBehaviourAndLongPolling(
        Config.botToken,
        CoroutineScope(Dispatchers.IO)
    ) {
        /* ---------------- JOIN REQUEST ---------------- */

        onChatJoinRequest {
            val chat = it.chat
            val userId = it.from.id
            val model = getModel(it.from.asCommonUser()?.ietfLanguageCode?.code)

            /* ---------------- AUTO TIMEOUT CLEANER ---------------- */

            val timeoutJob = launch {
                delay(timeoutMs)

                val pending = requestMap.remove(userId)
                if (pending != null) {
                    bot.sendMessage(userId, model.timeout)
                    bot.declineChatJoinRequest(pending.chat.id, userId)
                    println("Auto-declined ${userId.chatId} (timeout)")
                }
            }

            requestMap[userId] = JoinRequest(
                chat = chat,
                userId = userId,
                timeoutJob = timeoutJob
            )

            bot.sendMessage(userId, model.question)
            println("Join request from ${userId.chatId} for ${chat.title}")
        }

        /* ---------------- HELP ---------------- */

        onCommandWithArgs("help") { msg, _ ->
            bot.sendMessage(
                msg.chat,
                getModel(msg.from?.asCommonUser()?.ietfLanguageCode?.code).usage
            )
        }

        /* ---------------- JOIN ANSWER ---------------- */

        onCommandWithArgs("join") { msg, args ->
            val user = msg.chat.asPrivateChat() ?: return@onCommandWithArgs
            val model = getModel(msg.from?.asCommonUser()?.ietfLanguageCode?.code)

            val req = requestMap[user.id]

            if (req == null) {
                bot.sendMessage(user.id, model.notFound)
                return@onCommandWithArgs
            } else if (args.size <= 1) {
                bot.sendMessage(user.id, model.usage)
                return@onCommandWithArgs
            }

            val userId = user.id.chatId
            val userName = user.firstName + " " + user.lastName
            val chatName = req.chat.title
            val answer = msg.content.text
                .removePrefix("/join")
                .trim()

            val accepted = answer.contains(
                Config.acceptedKeyword,
                ignoreCase = true
            )

            val logMessage: String

            if (accepted) {
                bot.approveChatJoinRequest(req.chat.id, user.id)
                bot.sendMessage(user.id, model.correct)

                logMessage = "#APPROVE:\n" +
                        "Chat: $chatName\n" +
                        "User: $userName\n" +
                        "User ID: $userId\n" +
                        "Response: $answer"
                println("Approved: $userName -> $chatName")
            } else {
                bot.declineChatJoinRequest(req.chat.id, user.id)
                bot.sendMessage(user.id, model.incorrect)

                logMessage = "#REJECT:\n" +
                        "Chat: $chatName\n" +
                        "User: $userName\n" +
                        "User ID: $userId\n" +
                        "Response: $answer"
                println("Rejected: $userName -> $chatName")
            }

            req.timeoutJob.cancel()
            requestMap.remove(user.id)

            Config.logGroupId?.let {
                bot.sendMessage(IdChatIdentifier(it), logMessage)
            }
        }
    }.second.join()
}