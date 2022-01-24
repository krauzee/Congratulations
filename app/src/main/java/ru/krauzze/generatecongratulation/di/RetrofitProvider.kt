package ru.krauzze.generatecongratulation.di

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.krauzze.generatecongratulation.BuildConfig
import ru.krauzze.generatecongratulation.data.network.ApiRequests
import javax.inject.Inject
import javax.inject.Provider

class RetrofitProvider @Inject constructor(private val client: OkHttpClient) : Provider<ApiRequests> {

    override fun get(): ApiRequests =
        Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .client(client)
            .addConverterFactory(
                // 2019-02-10 18:33:26
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create()
                )
            )
            .build()
            .create(ApiRequests::class.java)
}