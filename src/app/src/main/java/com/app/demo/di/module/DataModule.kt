package com.app.demo.di.module

import android.app.Application
import com.app.demo.repository.DataRepository
import com.app.remote.ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideDataRepository(services: ApiClient, app: Application): DataRepository {
        return DataRepository(services, app)
    }

}