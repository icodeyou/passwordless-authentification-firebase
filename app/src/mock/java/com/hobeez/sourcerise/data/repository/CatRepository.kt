package com.hobeez.sourcerise.data.repository

import android.content.Context
import com.hobeez.sourcerise.R
import com.hobeez.sourcerise.domain.entity.Cat
import com.hobeez.sourcerise.domain.service.CatService
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Single
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType

/**
 * TODO Description
 */
class CatRepository(retrofit: Retrofit, context: Context, moshi: Moshi) : CatService {

    private val mockNotesListJson = context.resources.openRawResource(R.raw.mock_cat)
        .bufferedReader().use { it.readText() }

    private val catType: ParameterizedType = Types.newParameterizedType(List::class.java, Cat::class.java)
    private val jsonAdapter = moshi.adapter<List<Cat>>(catType)
    private val mockCats = jsonAdapter.fromJson(mockNotesListJson)

    override fun getCat(secretKey: String): Single<Cat> {
        return Single.just(mockCats?.firstOrNull())
    }

    override fun getCats(limit: Int): Single<List<String>> {
        return Single.just(mockCats).map { kittens ->
            kittens.map { it.id }
        }
    }
}
