package it.unito.piscastore.controller

import it.unito.piscastore.model.Order
import it.unito.piscastore.model.ProductAuthor
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface OrderService {

    @GET("bag/user/{id}")
    fun getOrderOfUser(@Path("id")id: Long): Call<List<Order>>

}