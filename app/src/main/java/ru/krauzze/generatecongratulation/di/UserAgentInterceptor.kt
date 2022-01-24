package ru.krauzze.generatecongratulation.di

import okhttp3.Interceptor
import okhttp3.Response
import ru.krauzze.generatecongratulation.BuildConfig
import java.io.IOException

class UserAgentInterceptor : Interceptor {

    companion object {
        private const val USER_AGENT = "User-Agent"
    }

    private val userAgent: String =
            "Rento Android " + "${BuildConfig.VERSION_NAME} "


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestWithUserAgent = request.newBuilder()
                .header(USER_AGENT, userAgent)
                .build()

        return chain.proceed(requestWithUserAgent)
    }
}