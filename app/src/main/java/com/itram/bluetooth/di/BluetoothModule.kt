package com.itram.bluetooth.di

import android.content.Context
import com.itram.bluetoothcore.data.BleTemperatureSensorDataSource
import com.itram.bluetoothcore.data.TemperatureSensorDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BluetoothModule {
    @Provides
    @Singleton
    fun provideTemperatureSensorDataSource(
        @ApplicationContext context: Context
    ): TemperatureSensorDataSource = BleTemperatureSensorDataSource(context)
}

