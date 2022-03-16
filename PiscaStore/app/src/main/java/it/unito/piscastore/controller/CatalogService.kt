package it.unito.piscastore.controller

import it.unito.piscastore.model.Product
import retrofit2.Call
import retrofit2.http.GET

interface CatalogService {

    @GET("catalog")
    fun getCatalog(): Call<List<Product>>
}