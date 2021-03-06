package it.unito.piscastore.controller

import it.unito.piscastore.model.Product
import it.unito.piscastore.model.ProductAuthor
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatalogService {

    @GET("catalog")
    fun getCatalog(): Call<List<Product>>

    @GET("catalog/category/{id}")
    fun getCatalogByCategory(@Path("id")id: Long): Call<List<Product>>

    @GET("products/{id}")
    fun getProductById(@Path("id")id: Long): Call<ProductAuthor>

    @GET("products")
    fun getProductsByAuthor(@Query("id") id: Long): Call<List<Product>>

    @GET("products/{id}")
    suspend fun getProductById2(@Path("id")id: Long): Response<ProductAuthor>

}