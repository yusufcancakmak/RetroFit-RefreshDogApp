package com.yusufcancakmak.refleshdogapp.api

import retrofit2.http.GET
//https://random.dog/woof.json
const val BASE_URL ="https://random.dog"
interface ApiRequest {

    @GET("/woof.json")
    suspend fun getRandomDog(): ApiData


}