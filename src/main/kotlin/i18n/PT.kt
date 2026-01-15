package i18n

val PT = Model(
    lang = "pt",
    question = """
Pergunta:
Qual é o nome completo do modelo do seu dispositivo?

Para responder, faça assim:
/join [nome do dispositivo]
(Exemplo: /join Xiaomi Redmi Note 7)

O pedido de ingresso será avaliado automaticamente!
""".trimIndent(),
    correct = "Bem-vindo! Seu pedido foi aprovado.",
    incorrect = "Desculpe! Você não está elegível para entrar no grupo.",
    usage = "Uso: /join [nome do dispositivo]",
    notFound = "Pedido de ingresso não encontrado, por favor, tente novamente.",
    timeout = "⏱ Seu pedido de ingresso expirou por não receber resposta.\n" +
            "Por favor, envie um novo pedido e responda à pergunta novamente."
)