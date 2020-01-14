package com.hobeez.sourcerise.domain.service

import com.hobeez.sourcerise.domain.entity.Cat
import io.reactivex.Single

/**
 *
 * This class is an interface defining all methods overriden by CatRepository
 *
 */
interface CatService {
    fun getCat(secretKey: String): Single<Cat>
    fun getCats(limit: Int): Single<List<String>>
}
