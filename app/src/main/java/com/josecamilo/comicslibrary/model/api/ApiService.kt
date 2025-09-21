package com.josecamilo.comicslibrary.model.api

import com.josecamilo.comicslibrary.BuildConfig
import com.josecamilo.comicslibrary.getHash
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private const val BASE_URL = "https://gateway.marvel.com/v1/public/"

    private fun getRetrofit(): Retrofit {
        val apiSecret = BuildConfig.MARVEL_SECRET
        val apiKey = BuildConfig.MARVEL_KEY

        val clientInterceptor = okhttp3.Interceptor { chain ->
            val original = chain.request()

            // Generate timestamp and hash at request time, not object creation time
            val ts = System.currentTimeMillis().toString()
            val hash = getHash(ts, apiSecret, apiKey)

            val url = original.url.newBuilder()
                .addQueryParameter("ts", ts)
                .addQueryParameter("apikey", apiKey)
                .addQueryParameter("hash", hash)
                .build()

            val request = original.newBuilder().url(url).build()
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder().addInterceptor(clientInterceptor).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val api: MarvelApi = getRetrofit().create(MarvelApi::class.java)
}
