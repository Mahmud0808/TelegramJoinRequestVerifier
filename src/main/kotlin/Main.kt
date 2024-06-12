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
import dev.inmo.tgbotapi.types.UserId
import dev.inmo.tgbotapi.utils.PreviewFeature
import dev.inmo.tgbotapi.utils.RiskFeature
import i18n.getModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * This is one of the most easiest bot - it will just print information about itself
 */

data class JoinRequest(
    val identifier: ChatIdentifier,
    val userId: UserId,
)

@OptIn(PreviewFeature::class, RiskFeature::class)
suspend fun main(vararg args: String) {
    val map = MaxSizeHashMap<ChatIdentifier, JoinRequest>(128)
    if (args.size != 1) {
        println("Usage: java -jar joinreqbot.jar {BotToken}")
        return
    }

    println("BotToken: ${args[0]}")

    telegramBotWithBehaviourAndLongPolling(args[0], CoroutineScope(Dispatchers.IO)) {
        onChatJoinRequest {
            val userId = it.from.id
            val chatId = it.chat.id
            val mapSize = map.size
            val model = getModel(it.from.asCommonUser()?.ietfLanguageCode?.code)

            bot.sendMessage(userId, model.question)
            map[userId] = JoinRequest(chatId, userId)

            println("user $userId start joining $chatId, map size: $mapSize")
        }

        onCommandWithArgs("join") { it, args ->
            val user = it.chat.asPrivateChat()!!
            val model = getModel(it.from?.asCommonUser()?.ietfLanguageCode?.code)

            println(it)

            if (args.size <= 1) {
                bot.sendMessage(user.id, model.usage)
                return@onCommandWithArgs
            }

            if (map.containsKey(user.id)) {
                val req = map[user.id]!!
                val message = it.content.text

                if (message.contains("pixel", ignoreCase = true)) {
                    bot.approveChatJoinRequest(req.identifier, user.id)
                    bot.sendMessage(it.chat, model.correct)

                    println("user ${user.id} joined ${req.identifier}")

                    map.remove(user.id)
                } else {
                    bot.declineChatJoinRequest(req.identifier, user.id)
                    bot.sendMessage(user.id, model.incorrect)

                    println("user ${user.id} join ${req.identifier} failed")
                }
            } else {
                bot.sendMessage(user.id, model.notFound)

                println("user ${user.id} not found in group")
            }
        }
    }.second.join()
}
