import dev.inmo.tgbotapi.types.UserId
import dev.inmo.tgbotapi.types.chat.PublicChat
import kotlinx.coroutines.Job

data class JoinRequest(
    val chat: PublicChat,
    val userId: UserId,
    val timeoutJob: Job
)