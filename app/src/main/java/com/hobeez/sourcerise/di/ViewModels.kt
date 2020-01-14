package com.hobeez.sourcerise.di

import com.hobeez.sourcerise.ui.ExampleViewModel
import com.hobeez.sourcerise.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { ExampleViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
}
