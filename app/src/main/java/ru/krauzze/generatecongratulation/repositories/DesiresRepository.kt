package ru.krauzze.generatecongratulation.repositories

import ru.krauzze.generatecongratulation.data.Reason
import ru.krauzze.generatecongratulation.data.network.ApiRequests
import ru.krauzze.generatecongratulation.data.network.pojo.response.DesireItem
import javax.inject.Inject
import kotlin.random.Random

class DesiresRepository @Inject constructor(private val api: ApiRequests) {

    suspend fun getBirthdayDesires(reason: Reason, length: Int): String {
        val desiresList = getApiRequestByReason(reason)
            .sortedBy { it.desires.length }
            .map {
                it.lengthPercent = it.lengthPercent * 100
                it
            }
            .filter { (length - 25) < it.lengthPercent && it.lengthPercent <= length }
            .map { it.desires }
        println(desiresList)
        val index = if (desiresList.size > 1) Random.nextInt(0, desiresList.size) else 0
        return desiresList[index]
    }

    private suspend fun getApiRequestByReason(reason: Reason): List<DesireItem> {
        return when (reason) {
            Reason.BIRTHDAY -> api.getBirthdayDesires().birthday
            Reason.WEDDING_ANNIVERSARY -> api.getBirthdayDesires().weddingAnniversary
            Reason.NEW_YEAR -> api.getBirthdayDesires().newYear
            Reason.MOTHERS_DAY -> api.getBirthdayDesires().mothersDay
            Reason.FATHERS_DAY -> api.getBirthdayDesires().fathersDay
            Reason.FEMALE_DAY -> api.getBirthdayDesires().femaleDay
            Reason.MALE_DAY -> api.getBirthdayDesires().maleDay
            else -> api.getRandomDesires().random
        }
    }
}