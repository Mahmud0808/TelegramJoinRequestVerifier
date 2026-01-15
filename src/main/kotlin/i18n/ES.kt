package i18n

val ES = Model(
    lang = "es",
    question = """
Pregunta:
¿Cuál es el nombre completo del modelo de tu dispositivo?

Para responder, escribe de esta manera:
/join [nombre del dispositivo]
(Ejemplo: /join Xiaomi Redmi Note 7)

¡Tu solicitud de unión será evaluada automáticamente!
""".trimIndent(),
    correct = "¡Bienvenido! Tu solicitud ha sido aprobada.",
    incorrect = "¡Lo sentimos! No eres elegible para unirte al grupo.",
    usage = "Uso: /join [nombre del dispositivo]",
    notFound = "Solicitud de unión no encontrada, por favor inténtalo de nuevo.",
    timeout = "⏱ Tu solicitud de unión ha expirado por no recibir respuesta.\n" +
            "Por favor, envía una nueva solicitud y responde la pregunta nuevamente."
)