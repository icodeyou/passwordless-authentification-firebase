package com.hobeez.passwordlessfirebase.di

import com.hobeez.passwordlessfirebase.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { LoginViewModel(get()) }
}
