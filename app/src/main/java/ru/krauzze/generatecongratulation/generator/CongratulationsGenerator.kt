package ru.krauzze.generatecongratulation.generator

import ru.krauzze.generatecongratulation.data.Configuration
import ru.krauzze.generatecongratulation.data.Gender
import ru.krauzze.generatecongratulation.data.Reason
import kotlin.random.Random

/**
 * Объект, который отвечает за генерацию текста поздравления
 */
object CongratulationsGenerator {

    private var configuration = Configuration()

    /**
     * Функция, которая отвечает за текст поздравления, средством вызова других функций
     * в зависимости от пришедшей конфигурации [Configuration]
     */
    fun generateCongratulation(config: Configuration?): String {
        this.configuration = config ?: Configuration()
        val congratulation = StringBuilder()
        when {
            configuration.name.isNotEmpty() && configuration.reason != Reason.NOT_CHOSEN ->
                congratulation.append(generateNameReason())
            configuration.name.isNotEmpty() -> congratulation.append(generateOnlyName())
            configuration.reason != Reason.NOT_CHOSEN -> congratulation
                .append(generateOnlyReason())
            else -> congratulation.append(generateRandomCongratulation())
        }
        return congratulation.toString()
    }

    /**
     * Функция, которая генерирует поздравление учитывая
     * повод [Configuration.reason] и имя [Configuration.name]
     */
    private fun generateNameReason(): String {
        return StringBuilder()
            .append(getFullSalutation())
            .append(generateCongratulationVerb())
            .append(" ")
            .append(getPronoun(Case.PARENTS))
            .append(" c ")
            .append(getReason())
            .append("! ")
            .append(getDesireByPlural().replaceFirstChar { it.uppercase() })
            .append(" ")
            .append(getPronoun(Case.GIVES))
            .append(" ")
            .append(getDesiresByReason())
            .toString()
    }

    /**
     * Функция, которая генерирует поздравление учитывая  имя [Configuration.name]
     */
    private fun generateOnlyName(): String {
        return StringBuilder()
            .append(getFullSalutation())
            .append(" ")
            .append(getRandomDesire())
            .toString()
    }

    /**
     * Функция, которая генерирует поздравление учитывая  повод [Reason]
     */
    private fun generateOnlyReason(): String {
        return StringBuilder()
            .append(getFullSalutation())
            .append(" ")
            .append(getDesireByPlural().replaceFirstChar { it.uppercase() })
            .append(" ")
            .append(getDesiresByReason())
            .toString()
    }

    private fun getFullSalutation(): String {
        return if (configuration.name.isNotEmpty()) {
            val salutationEpithet =
                if (configuration.gender != Gender.UNDEFINED)
                    getSalutationEpithet(configuration.gender)
                else ""
            StringBuilder().append(salutationEpithet)
                .append(if (salutationEpithet.isNotEmpty()) " " else "")
                .append(configuration.name.replaceFirstChar { it.uppercase() })
                .append(if (salutationEpithet.isNotEmpty()) "! " else ", ")
                .toString()
        } else if (configuration.name.isEmpty() && configuration.reason != Reason.NOT_CHOSEN) {
            StringBuilder()
                .append("Поздравляю с ")
                .append(getReason())
                .append("!")
                .toString()
        } else getRandomSalutation()
    }

    /**
     * Функция, которая возвращает глагол-поздравление, например "Искренне поздравляю" и т.п.
     */
    private fun generateCongratulationVerb(): String {
        val congratulationVerbsList = listOf(
            "\n${getCongratulationPronounByPlural()} искренне ${getCongratulationVerbByPlural()} ",
            getCongratulationVerbByPlural().replaceFirstChar { it.uppercase() },
            "Искренне ${getCongratulationVerbByPlural()}",
        )
        return congratulationVerbsList[Random.nextInt(0, congratulationVerbsList.size)]
    }

    /**
     * Функция, которая возвращает обращение (например: "Уважаемый"/"Уважаемая" и т.п.) в
     * зависимости от пола человека
     */
    private fun getSalutationEpithet(gender: Gender): String {
        return when (gender) {
            Gender.MALE -> getMaleSalutation()
            Gender.FEMALE -> getFemaleSalutation()
            else -> ""
        }
    }

    /**
     * Функция, которая возвращает мужское обращение (например: "Уважаемый" и т.п.)
     */
    private fun getMaleSalutation(): String {
        return when {
            configuration.isOfficial -> "Уважаемый"
            configuration.closedOne -> "Любимый"
            else -> "Дорогой"
        }
    }

    /**
     * Функция, которая возвращает женское обращение (например: "Уважаемая" и т.п.)
     */
    private fun getFemaleSalutation(): String {
        return when {
            configuration.isOfficial -> "Уважаемая"
            configuration.closedOne -> "Любимая"
            else -> "Дорогая"
        }
    }

    /**
     * Функция, которая возвращает местоимение Я/Мы в зависимости от число(множественное или единственное) @see [Configuration.isFromWe]
     */
    private fun getCongratulationPronounByPlural(): String {
        return if (configuration.isFromWe) "Мы" else "Я"
    }

    /**
     * Функция, которая возвращает глагол поздравляю в зависимости от число(множественное или единственное) @see [Configuration.isFromWe]
     */
    private fun getCongratulationVerbByPlural(): String {
        return if (configuration.isFromWe) "поздравляем" else "поздравляю"
    }

    /**
     * Функция, которая возвращает слово "Желаем" в зависимости от число(множественное или единственное) @see [Configuration.isFromWe]
     */
    private fun getDesireByPlural(): String {
        return if (configuration.isFromWe) "желаем" else "желаю"
    }

    /**
     * Функция, которая возвращает местоимение, в зависимости от падежа и официальности
     */
    private fun getPronoun(case: Case): String {
        return when (case) {
            Case.NAMES -> if (configuration.isOfficial) "Вы" else "ты"
            Case.PARENTS -> if (configuration.isOfficial) "Вас" else "тебя"
            Case.GIVES -> if (configuration.isOfficial) "Вам" else "тебe"
            Case.ACCUSE -> if (configuration.isOfficial) "Вашу" else "твою"
            Case.PREPOSITIONAL -> if (configuration.isOfficial) "Ваша" else "твоя"
        }
    }

    /**
     * Функция, которая возвращает повод в виде понятной строки
     */
    private fun getReason(): String {
        return when (configuration.reason) {
            Reason.BIRTHDAY -> "днём рождения"
            Reason.WEDDING_ANNIVERSARY -> "годовщиной свадьбы"
            Reason.NEW_YEAR -> "новым годом"
            Reason.MOTHERS_DAY -> "днём Матери"
            Reason.FATHERS_DAY -> "днём Отца"
            Reason.FEMALE_DAY -> "8 марта"
            Reason.MALE_DAY -> "днём защитника отечества"
            Reason.INDEPENDENCE_DAY -> "днём независимости"
            Reason.UNDEFINED -> "(ПОВОД)"
            Reason.NOT_CHOSEN -> ""
        }
    }

    /**
     * Функция, которая генерирует пожелания в зависимости от выбранного повода
     */
    private fun getDesiresByReason(): String {
        return when (configuration.reason) {
            Reason.BIRTHDAY -> getDesiresByLengthForTenSizeList(birthdayDesiresList)
            Reason.WEDDING_ANNIVERSARY -> getDesiresByLength(anniversaryDesiresList)
            Reason.NEW_YEAR -> getDesiresByLength(newYearDesiresList)
            Reason.MOTHERS_DAY -> getDesiresByLength(mothersDayDesiresList)
            Reason.FATHERS_DAY -> getDesiresByLength(fathersDayCongratulationList)
            Reason.FEMALE_DAY -> getDesiresByLength(femaleDayDesiresList)
            Reason.MALE_DAY -> getDesiresByLength(maleDayDesiresList)
            Reason.INDEPENDENCE_DAY -> INDEPENDENCE_DESIRES
            Reason.UNDEFINED -> "(ПОВОД)"
            Reason.NOT_CHOSEN -> ""
        }
    }

    /**
     * Функция, которая берёт рандомное пожелание из @congratulationList.
     * Используется, когда генератор получает конфигурацию == null
     * или когда пользователь не ввел имя
     */
    private fun generateRandomCongratulation(): String {
        return StringBuilder()
            .append(getRandomSalutation())
            .append(" ")
            .append(getRandomDesire())
            .toString()
    }

    private fun getRandomSalutation(): String {
        return randomSalutationList[Random.nextInt(0, randomSalutationList.size - 1)]
    }

    /**
     * Список обезличенных поздравлений, которые, так же, не привязаны к ПОВОДУ, полу и официальности
     */
    private val randomSalutationList = listOf(
        "Поздравляю с праздником!",
        "С праздником!",
        "От всей души поздравляю!",
        "Искренне поздравляю!"
    )

    private fun getRandomDesire(): String {
        return getDesiresByLengthForTenSizeList(randomDesiresList)
    }

    /**
     * Список обезличенных пожеланий, которые, так же, не привязаны к ПОВОДУ, полу и официальности
     */
    private val randomDesiresList = listOf(
        "Желаю счастья и здоровья!",
        "Пусть всё складывается удачно, желаю успехов и процветания!",
        "Желаю всего наилучшего! Пусть жизнь бьет ключом, а в любом начинании сопутствует удача и успех!",
        "Желаю осуществления всего задуманного, тепла и гармонии дома, здоровья, карьерных успехов и верных друзей рядом!",
        "Пусть желания исполняются, достижение целей будет только в удовольствие, трудности пусть будут приятными и все задуманное воплощается в жизнь в кротчайшие сроки!",
        "Желаю осуществления всего задуманного, тепла и гармонии дома, здоровья, успехов и верных друзей рядом! Пусть в семье всегда будет полная чаша, а достаток только увеличивается!",
        "Желаю в этот день много радостных и позитивных моментов, пусть тревоги обходят стороной, родные и близкие люди будут рядом, здоровье всегда будет крепким и никогда не подводит!",
        "Пусть желания исполняются, достижение целей будет только в удовольствие, трудности пусть будут приятными и все задуманное воплощается в жизнь в кротчайшие сроки!",
        "Желаю осуществления всего задуманного, тепла и гармонии дома, здоровья, успехов и верных друзей рядом! Пусть в семье всегда будет полная чаша, а достаток только увеличивается!",
        "Желаю в этот день много радостных и позитивных моментов, пусть тревоги обходят стороной, родные и близкие люди будут рядом, здоровье всегда будет крепким и никогда не подводит!"
    )

    /**
     * Список пожеланий ко дню рождения
     */
    private val birthdayDesiresList = listOf(
        "счастья и здоровья!",
        "крепкого здоровья и большого счастья!",
        "крепкого здоровья, счастья и исполнения желаний!",
        "крепкого здоровья, счастья, пусть в этот день все загаданные желания исполняются!",
        "счастья, здоровья, нескончаемой энергии для достижения всех целей и воплощения мечт в жизнь!",
        "большого счастья, крепкого здоровья, пусть жизнь бьет ключом и будет полна позитивных событий!",
        "здоровья, огромного счастья, пусть все загаданные в этот день желания непременно исполнятся и каждый день будет наполнен теплом и радостью!",
        "крепкого здоровья, большого счастья, нескончаемой энергии для достижения всех целей, пусть все загаданные в этот день желания непременно исполнятся!",
        "крепкого здоровья, большого счастья, нескончаемой энергии для достижения всех целей, пусть все загаданные в этот день желания непременно исполнятся а жизнь бьет ключом!",
        "крепкого здоровья, большого счастья, нескончаемой энергии для достижения всех целей, пусть все загаданные в этот день желания непременно исполнятся а жизнь будет наполнена позитивными и радостными событиями!"
    )

    /**
     * Список пожеланий, на годовщину
     */
    private val anniversaryDesiresList = listOf(
        "крепкой любви и взаимопонимания!",
        "взаимопонимания, терпения и, конечно же, крепкой любви!",
        "крепкой и нескончаемой любви, семейного тепла и уюта, а так же взаимопонимания и терпения!",
        "вашей семье любви, домашнего тепла и уюта, взаимопонимания, пусть ваши отношения с годами становятся крепче и ближе!",
        "чтобы в вашей семье царили уют, тепло и взаимопонимание, пусть любовь с каждым годом становится сильнее, а счастье больше!"
    )

    /**
     * Список пожеланий, на новый год
     */
    private val newYearDesiresList = listOf(
        ", чтобы новый год был еще лучше предыдущего!",
        "в новом году достигнуть всех поставленных целей!",
        "крепкой и нескончаемой любви, семейного тепла и уюта, а так же взаимопонимания и терпения!",
        ", чтобы в новом году было как можно больше позитивных моментов, пусть проблемы обходят стороной, а жизнь бьет ключом!",
        "чтобы в вашей семье царили уют, тепло и взаимопонимание, пусть любовь с каждым годом становится сильнее, а счастье больше!"
    )

    /**
     * Список пожеланий, на день матери
     */
    private val mothersDayDesiresList = listOf(
        "счастья, крепкого здоровья, пусть родные и близкие будут рядом!",
        "крепкого здоровья, пусть ${getPronoun(Case.PREPOSITIONAL)} любовь возвращается к ${getPronoun(Case.GIVES)} в десятикратном размере!",
        "крепкого здоровья, спасибо за всю поддержку и любовь и просто за то, что ${getPronoun(Case.NAMES)} есть!",
        "${getPronoun(Case.GIVES)} крепкого здоровья, душевного спокойствия, спасибо за всю поддержку и любовь и просто за то, что ${getPronoun(Case.NAMES)} есть!${if (!configuration.isOfficial) I_LOVE_YOU_STRING else ""}",
        "крепкого здоровья, радости и тепла, спасибо за ${getPronoun(Case.ACCUSE)} бесконечную любовь всю поддержку и любовь и просто за то, что ${getPronoun(Case.NAMES)} есть!${if (!configuration.isOfficial) I_LOVE_YOU_STRING else ""}",
    )

    /**
     * Список пожеланий, на день отца
     */
    private val fathersDayCongratulationList = listOf(
        "здоровья, достатка и всего наилучшего!",
        "крепкого здоровья и стабильного достатка и всего наилучшего!",
        "крепкого здоровья, любви, пусть в жизни будет как можно больше позитивных моментов!",
        "крепкого здоровья, стабильного достатка и пусть все в жизни ладится так, как ${getPronoun(Case.GIVES)} хочется!",
        "любви, крепкого здоровья, пусть в жизни будет как можно больше позитивных моментов и чтобы родные были рядом!"
    )

    /**
     * Список пожеланий, на 8 марта
     */
    private val femaleDayDesiresList = listOf(
        "счастья, тепла и любви!",
        "любви, счастья, тепла и побольше букетов!",
        "большой любви, легкости, позитива и простого женского счастья!",
        "чтобы ${getPronoun(Case.PARENTS)} как можно чаще окружала любовь, забота и понимание близких, и, конечно же, любви и достатка!",
        "много цветов, большой и крепкой любви, счастья, пусть сегодня и не только сегодня жизнь будет наполнена теплом, уютом и заботой!"
    )

    /**
     * Список пожеланий, на 23 февраля
     */
    private val maleDayDesiresList = listOf(
        "крепкого здоровья и мирного неба над головой!",
        "крепкого здоровья, сил, мужества и мирного неба над головой!",
        "крепкого здоровья, душевного спокойствия, мужества и мирного неба над головой!",
        "крепкого здоровья, любви родных и близких и мирного неба над головой!",
        "крепкого здоровья, мирного неба над головой, пусть все в жизни будет замечательно!"
    )

    private fun getDesiresByLength(targetDesiresList: List<String>): String {
        return when (configuration.lengthDegree) {
            0 -> targetDesiresList[0]
            1 -> targetDesiresList[1]
            3 -> targetDesiresList[3]
            4 -> targetDesiresList[4]
            else -> targetDesiresList[Random.nextInt(2, 3)]
        }
    }

    private fun getDesiresByLengthForTenSizeList(targetDesiresList: List<String>): String {
        return when (configuration.lengthDegree) {
            0 -> targetDesiresList[Random.nextInt(0, 1)]
            1 -> targetDesiresList[Random.nextInt(2, 3)]
            3 -> targetDesiresList[Random.nextInt(6, 7)]
            4 -> targetDesiresList[Random.nextInt(8, 9)]
            else -> targetDesiresList[Random.nextInt(3, 7)]
        }
    }
}

private const val INDEPENDENCE_DESIRES = " ....а кто-то кого-то вообще поздравляет с этим и когда вообще этот праздник? А чего желать? Побольше независимости!!XDDD"
private const val I_LOVE_YOU_STRING = "\nЯ тебя люблю!"

/**
 * Падежи
 * NAMES == именительный
 * PARENTS == родительный
 * GIVES == дательный
 * ACCUSE == винительный
 * PREPOSITIONAL == предложный
 */
enum class Case {
    NAMES, PARENTS, GIVES, ACCUSE, PREPOSITIONAL
}