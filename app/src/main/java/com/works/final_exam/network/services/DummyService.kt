package com.works.final_exam.network.services

import com.works.final_exam.models.CartResponse
import com.works.final_exam.models.JWTUser
import com.works.final_exam.models.Product
import com.works.final_exam.models.Products
import com.works.final_exam.models.UpdateUserRequest
import com.works.final_exam.models.UserSend
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface DummyService {

    @POST("auth/login")
    fun login( @Body userSend: UserSend) : Call<JWTUser>

    @GET("products/categories")
    fun getCategories(): Call<List<String>>

    @GET("products/category/{selectedCategory}")
    fun getSelectedCategory(@Path("selectedCategory") selectedCategory: String): Call<Products>

    @GET("carts/user/{id}")
    fun getUserCart(@Path("id") id: String): Call<CartResponse>

    @GET("products/search?")
    fun productSearch(@Query("q") query: String): Call<Products>

    @Headers("Content-Type: application/json")
    @POST("products/add")
    fun addProduct(@Body product: Product): Call<Void>

    @Headers("Content-Type: application/json")
    @PUT("users/{id}")
    fun updateUser(@Path("id") userId: String, @Body request: UpdateUserRequest): Call<JWTUser>



}