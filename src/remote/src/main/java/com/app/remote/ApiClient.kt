package com.app.remote

import com.app.remote.model.ProductsDto
import com.app.remote.model.SessionResponseDto
import retrofit2.Response
import retrofit2.http.*

interface ApiClient {

    @POST("user/refreshToken/")
    @FormUrlEncoded
    suspend fun refreshToken(
        @Field("refreshToken") refreshToken: String
    ): Response<SessionResponseDto>

    @GET("/sites/MLU/search")
    suspend fun search(
        @Query("q") string: String
    ): Response<ProductsDto>

}
