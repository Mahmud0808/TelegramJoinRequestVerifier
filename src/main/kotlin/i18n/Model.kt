package i18n

data class Model(
    val lang : String,
    val question : String,
    val correct : String,
    val incorrect: String,
    val usage: String,
    val notFound: String,
    val timeout: String,
)

val allLang = arrayOf(
    EN,
    AR,
    DE,
    ES,
    FR,
    HI,
    JA,
    KO,
    PT,
    RU,
    ZH
)

fun getModel(lang: String?): Model {
    lang?.let {
        for (one in allLang){
            if (lang.lowercase().contains(one.lang)){
                return one
            }
        }
    }
    return EN
}