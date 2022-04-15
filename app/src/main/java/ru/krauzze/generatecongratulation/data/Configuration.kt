package ru.krauzze.generatecongratulation.data

import ru.krauzze.generatecongratulation.R

/**
 * Клас настройки генерируемого поздравления
 * @param name - имя поздравляемого человека
 * @param reason - повод, для которого нужно сгенерировать поздравление
 * @param gender - пол поздравляемого человека, Вычисляется автоматически, но можно скорректировать вручную
 * @param closedOne - флаг, который указывает, является ли человек близким
 * @param isOfficial - тогл официального поздравления, влияет на то какие местоимения будут использоваться ТЫ/ВЫ
 * @param lengthDegree - длина пожеланий, в зависимости от выбранной позиции слайдера 0-100 с шагом 25
 * @param isFromWe - флаг, который указывает, как будет выражено поздравление, от "меня" или от "нас", например "желаю" или "желаем"
 *
// * @param salutation - обращение к человеку, например "дорогой ДРУГ" или "любимая МАМА", здесь друг и мама являются salutation
// * @param isPoem - поздравление в стихах
// * @param closeDegree степень близости человека, влияет на то, насколько душевное будет поздравление 0-100
// * @param humorDegree степень юмористичности поздравления 0-4
 */
data class Configuration(
    val name: String = "",
    val reason: Reason = Reason.NOT_CHOSEN,
    val gender: Gender = Gender.UNDEFINED,
    val closedOne: Boolean = false,
    val isOfficial: Boolean = false,
    val lengthDegree: Int = 50,
    val isFromWe: Boolean = false,


//    val needSalutationEpithet: Boolean = true,
//    val salutation: String = "",
//    val isPoem: Boolean = false,
//    val closeDegree: Int = 0,
//    val humorDegree: Int = 30
)

enum class Gender {
    MALE, FEMALE, UNDEFINED
}

enum class Reason(val value: Int) {
    BIRTHDAY(R.string.birthday),
    WEDDING_ANNIVERSARY(R.string.wedding_anniversary),
    NEW_YEAR(R.string.new_year),
    MOTHERS_DAY(R.string.mothers_day),
    FATHERS_DAY(R.string.fathers_day),
    FEMALE_DAY(R.string.female_day),
    MALE_DAY(R.string.male_day),
    INDEPENDENCE_DAY(R.string.independence_day),
    UNDEFINED(R.string.undefined),
    NOT_CHOSEN(R.string.not_chosen)
}

const val C_LENGTH_STEPS_COUNT = 3
const val C_LENGTH_DEF_VALUE = 2f
val C_RANGE_STEPS_COUNT = 0f..4f
