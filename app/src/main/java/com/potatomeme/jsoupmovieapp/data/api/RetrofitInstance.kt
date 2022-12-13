package com.potatomeme.jsoupmovieapp.data.api

import com.potatomeme.jsoupmovieapp.util.Constants.API_BASE
import com.potatomeme.jsoupmovieapp.util.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val okHttpClient by lazy {
        val httpLoginingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(httpLoginingInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(API_BASE)
            .build()
    }

    val api: MovieRankingApi by lazy {
        retrofit.create(MovieRankingApi::class.java)
    }
}