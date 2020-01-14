package com.hobeez.sourcerise.di

import com.hobeez.sourcerise.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import java.util.Date
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {

    // Initialise the network
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASEURL)
            .client(get<OkHttpClient>())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    // Initialize OK HTTP Client
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .apply {
                addInterceptor { chain ->
                    val requestBuilder = chain.request().newBuilder()
                    requestBuilder.header("x-api-key", "61e1f008-a969-4033-866a-8d811da183c7")
                    chain.proceed(requestBuilder.build())
                }

                val logger = HttpLoggingInterceptor()
                logger.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(logger)
            }.build()
    }

    // Initialize Moshi
    single<Moshi> {
        Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .build()
    }
}
