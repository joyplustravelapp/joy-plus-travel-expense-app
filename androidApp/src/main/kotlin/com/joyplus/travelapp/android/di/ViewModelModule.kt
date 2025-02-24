package com.joyplus.travelapp.android.di

import com.joyplus.travelapp.android.ui.expenses.ExpenseViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for ViewModels.
 */
val viewModelModule = module {
    viewModel { ExpenseViewModel(get()) }
    // Add other ViewModels here as they are created
}