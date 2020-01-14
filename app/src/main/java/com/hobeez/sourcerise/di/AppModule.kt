package com.hobeez.sourcerise.di

import com.hobeez.sourcerise.data.repository.CatRepository
import com.hobeez.sourcerise.domain.service.CatService
import org.koin.dsl.module

val servicesModule = module {
    single<CatService> { CatRepository(get(), get(), get()) }
}
