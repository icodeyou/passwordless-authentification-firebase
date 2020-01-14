package com.hobeez.sourcerise.data.api

import com.hobeez.sourcerise.domain.entity.Cat
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *
 * This is where we write the routes to get informations concerning the model Cat
 *
 */
interface CatApi {
    @GET("v1/images/search")
    fun getCat(
        @Query("parameter") secretKey: String = "value"
    ): Single<List<Cat>>

    @GET("v1/images/search")
    fun getKittens(
        @Query("limit") limit: String = "3"
    ): Single<List<Cat>>
}
