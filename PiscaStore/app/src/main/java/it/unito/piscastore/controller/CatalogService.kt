package it.unito.piscastore.controller

import it.unito.piscastore.model.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CatalogService {

    @GET("catalog")
    fun getCatalog(): Call<List<Product>>

    @GET("catalog/category/{id}")
    fun getCatalogByCategory(@Path("id")id: Long): Call<List<Product>>
}