package com.stocardapp.hackschoolchat.backend

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

object Backend {

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor { Timber.tag("OkHttp").v(it) }
                        .apply { level = HttpLoggingInterceptor.Level.BODY }).build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://android-hackschool.herokuapp.com")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
    }

    val instance: BackendService by lazy {
        retrofit.create<BackendService>(BackendService::class.java)
    }

}