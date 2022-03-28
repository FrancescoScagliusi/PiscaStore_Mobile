package it.unito.piscastore.controller

import android.telecom.Call
import it.unito.piscastore.model.CurrentInfo
import it.unito.piscastore.model.CurrentUser
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.Headers

interface AccountService {

    @GET("current/info")
    fun getCurrentUser(
            @Header("Authorization") token: String
    ): retrofit2.Call<CurrentInfo?>?
}