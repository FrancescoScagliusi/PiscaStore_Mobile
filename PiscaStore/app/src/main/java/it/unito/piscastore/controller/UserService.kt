package it.unito.piscastore.controller

import it.unito.piscastore.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @POST("signin")
    fun login(@Body user: SigninUser): Call<CurrentUser?>

    @POST("signup")
    fun register(@Body user: SignupUser?): Call<CurrentUser?>?

}