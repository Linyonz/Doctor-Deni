package com.example.doctordeni.di

import com.example.doctordeni.viewmodels.AuthViewModel
import com.example.doctordeni.viewmodels.ContentViewModel
import com.example.doctordeni.viewmodels.LoanProviderViewModel
import com.example.doctordeni.viewmodels.StrategyViewModel
import com.example.doctordeni.viewmodels.TrackerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewmodels = module {

    viewModel { AuthViewModel(get()) }
    viewModel { TrackerViewModel(get(),get()) }
    viewModel { ContentViewModel(get(),get()) }
    viewModel { StrategyViewModel(get(),get()) }
    viewModel { LoanProviderViewModel()}
}
