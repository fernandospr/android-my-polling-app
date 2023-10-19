package com.github.fernandospr.mypollingapplication

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PersonApi {
    @POST("/person")
    suspend fun postPerson(@Body person: Person): Response<Unit>

    @GET("/person/{code}/status")
    suspend fun getPostPersonStatus(@Path("code") code: String): Response<Person>
}
