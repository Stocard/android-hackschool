package com.stocardapp.hackschoolchat.backend

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET


interface Backend {

    @GET("/")
    fun listRepos(): Response<String>

}

var retrofit = Retrofit.Builder()
        .baseUrl("http://localhost")
        .build()

var service = retrofit.create<Backend>(Backend::class.java)