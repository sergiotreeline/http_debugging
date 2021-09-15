package com.app.demo.di.module

import android.content.Context
import android.net.ConnectivityManager
import com.app.remote.ApiClient
import com.app.remote.core.ServiceFactory
import com.app.remote.network.ConnectionStateMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiClient(@ApplicationContext context: Context): ApiClient {
        return ServiceFactory.createService(context = context)
    }

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    fun provideConnectionStateMonitor(@ApplicationContext context: Context): ConnectionStateMonitor {
        return ConnectionStateMonitor(context)
    }

}
