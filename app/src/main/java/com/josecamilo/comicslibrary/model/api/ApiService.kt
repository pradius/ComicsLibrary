package com.josecamilo.comicslibrary.model.api

import com.josecamilo.comicslibrary.BuildConfig
import com.josecamilo.comicslibrary.getHash
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private const val BASE_URL = "https://gateway.marvel.com/v1/public/"

    private fun getRetrofit(): Retrofit {
        val ts = System.currentTimeMillis()
        val apiSecret = BuildConfig.MARVEL_SECRET
        val apiKey = BuildConfig.MARVEL_KEY
        val hash = getHash(ts.toString(), apiSecret, apiKey)

        val clientInterceptor = okhttp3.Interceptor { chain ->
            val original = chain.request()

            val url = original.url.newBuilder()
                .addQueryParameter("ts", ts.toString())
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