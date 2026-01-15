package i18n

val FR = Model(
    lang = "fr",
    question = """
Question :
Quel est le nom complet du modèle de votre appareil ?

Pour répondre, faites ainsi :
/join [nom de l'appareil]
(Exemple : /join Xiaomi Redmi Note 7)

Votre demande d'adhésion sera évaluée automatiquement !
""".trimIndent(),
    correct = "Bienvenue ! Votre demande a été approuvée.",
    incorrect = "Désolé ! Vous n'êtes pas éligible pour rejoindre le groupe.",
    usage = "Utilisation : /join [nom de l'appareil]",
    notFound = "Demande d'adhésion introuvable, veuillez réessayer.",
    timeout = "⏱ Votre demande d'adhésion a expiré faute de réponse.\n" +
            "Veuillez envoyer une nouvelle demande et répondre à la question à nouveau."
)