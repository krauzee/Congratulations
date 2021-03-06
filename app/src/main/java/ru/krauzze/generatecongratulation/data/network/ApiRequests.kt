package ru.krauzze.generatecongratulation.data.network

import retrofit2.http.GET
import ru.krauzze.generatecongratulation.data.network.pojo.response.DesiresResponse

interface ApiRequests {

    @GET("birthday")
    suspend fun getBirthdayDesires(): DesiresResponse

    @GET("random")
    suspend fun getRandomDesires(): DesiresResponse
}