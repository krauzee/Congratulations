package ru.krauzze.generatecongratulation.di

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.krauzze.generatecongratulation.BuildConfig
import ru.krauzze.generatecongratulation.data.network.ApiRequests
import toothpick.config.Module
import java.util.concurrent.TimeUnit

class AppModule(context: Context) : Module() {

    init {
        //Global
        bind(Context::class.java).toInstance(context)

        //Repositories
        //bind(AuthorizationRepository::class.java).to(AuthorizationRepositoryImpl::class.java)

        //Retrofit
        bind(ApiRequests::class.java).toProvider(RetrofitProvider::class.java)

        val client = with(OkHttpClient.Builder().addInterceptor(UserAgentInterceptor())) {
            connectTimeout(20, TimeUnit.SECONDS)
            readTimeout(20, TimeUnit.SECONDS)

            if (BuildConfig.DEBUG) {
                addNetworkInterceptor(
                    HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
                )
            }

            // Auth
            addInterceptor(Interceptor { chain ->
                val original = chain.request()

                val request = original.newBuilder()
                //todo add auth if needed
                //                    val token = DataStore.Token.token
                //                    if (!token.isNullOrEmpty()) {
                //                        request.header("Authorization", "Bearer $token")
                //                    }
                chain.proceed(request.build())
            })
            build()
        }

        bind(OkHttpClient::class.java).toInstance(client)

    }

}