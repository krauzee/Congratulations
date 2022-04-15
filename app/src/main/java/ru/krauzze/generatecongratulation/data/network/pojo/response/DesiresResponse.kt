package ru.krauzze.generatecongratulation.data.network.pojo.response

data class DesiresResponse(
    val birthday: List<DesireItem> = listOf(),
    val random: List<DesireItem> = listOf(),
    val weddingAnniversary: List<DesireItem> = listOf(),
    val newYear: List<DesireItem> = listOf(),
    val mothersDay: List<DesireItem> = listOf(),
    val fathersDay: List<DesireItem> = listOf(),
    val femaleDay: List<DesireItem> = listOf(),
    val maleDay: List<DesireItem> = listOf()
)
