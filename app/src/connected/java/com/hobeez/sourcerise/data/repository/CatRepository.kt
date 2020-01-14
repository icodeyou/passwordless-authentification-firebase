package com.hobeez.sourcerise.data.repository

import android.content.Context
import com.hobeez.sourcerise.data.api.CatApi
import com.hobeez.sourcerise.domain.entity.Cat
import com.hobeez.sourcerise.domain.service.CatService
import com.squareup.moshi.Moshi
import io.reactivex.Single
import retrofit2.Retrofit

/**
 * TODO Description
 */
class CatRepository(retrofit: Retrofit, context: Context, moshi: Moshi) : CatService {
    private val catApi = retrofit.create(CatApi::class.java)

    override fun getCat(secretKey: String): Single<Cat> {
        return catApi.getCat(secretKey).map { it.firstOrNull() }
    }

    override fun getCats(limit: Int): Single<List<String>> {
        return catApi.getKittens(limit.toString()).map { kittens ->
            kittens.map { it.id }
        }
    }
}
