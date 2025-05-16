package com.rodcibils.sfmobiletest.di

import com.rodcibils.sfmobiletest.api.HttpClientProvider
import com.rodcibils.sfmobiletest.api.RemoteSeedDataSource
import com.rodcibils.sfmobiletest.local.LocalSeedDataSource
import com.rodcibils.sfmobiletest.repo.SeedRepository
import com.rodcibils.sfmobiletest.ui.screen.qrcode.QRCodeViewModel
import com.rodcibils.sfmobiletest.ui.screen.scan.ScanViewModel
import io.ktor.client.HttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModel { QRCodeViewModel(get()) }
        viewModel { ScanViewModel(get()) }
    }

val repositoryModule =
    module {
        single { RemoteSeedDataSource(get()) }
        single { LocalSeedDataSource(get()) }
        single { SeedRepository(get(), get()) }
    }

val networkModule =
    module {
        single<HttpClient> {
            HttpClientProvider.client
        }
        single {
            RemoteSeedDataSource(get())
        }
    }
