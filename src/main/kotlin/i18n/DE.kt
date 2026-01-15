package i18n

val DE = Model(
    lang = "de",
    question = """
Frage:
Wie lautet der vollständige Modellname Ihres Geräts?

Antworten Sie folgendermaßen:
/join [Gerätename]
(Beispiel: /join Xiaomi Redmi Note 7)

Die Beitrittsanfrage wird automatisch überprüft!
""".trimIndent(),
    correct = "Willkommen! Deine Anfrage wurde genehmigt.",
    incorrect = "Entschuldigung! Du bist nicht berechtigt, der Gruppe beizutreten.",
    usage = "Verwendung: /join [Gerätename]",
    notFound = "Beitrittsanfrage nicht gefunden, bitte versuchen Sie es erneut.",
    timeout = "⏱ Ihre Beitrittsanfrage ist aufgrund fehlender Antwort abgelaufen.\n" +
            "Bitte senden Sie eine neue Anfrage und beantworten Sie die Frage erneut."
)