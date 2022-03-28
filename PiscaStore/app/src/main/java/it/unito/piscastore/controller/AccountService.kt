package it.unito.piscastore.controller

import it.unito.piscastore.model.Address
import it.unito.piscastore.model.CurrentInfo
import it.unito.piscastore.model.CurrentUser
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface AccountService {

    @GET("current/info")
    fun getCurrentUser(
            @Header("Authorization") token: String
    ): Call<CurrentInfo?>?

    @GET("address/{id}")
    suspend fun getAddressById(@Path("id") id: Long): Response<Address>

}