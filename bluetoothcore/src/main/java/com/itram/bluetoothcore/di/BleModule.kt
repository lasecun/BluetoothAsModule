package com.itram.bluetoothcore.di

import android.content.Context
import com.itram.bluetoothcore.data.BleTemperatureSensorDataSource
import com.itram.bluetoothcore.data.repository.BleScanRepositoryImpl
import com.itram.bluetoothcore.domain.repository.BleScanRepository
import com.itram.bluetoothcore.domain.usecase.ScanBleDevicesUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BleModule {
    @Binds
    abstract fun bindBleScanRepository(
        bleScanRepositoryImpl: BleScanRepositoryImpl
    ): BleScanRepository

    companion object {
        @Provides
        @Singleton
        fun provideScanBleDevicesUseCase(
            repository: BleScanRepository
        ): ScanBleDevicesUseCase = ScanBleDevicesUseCase(repository)

        @Provides
        fun provideBleTemperatureSensorDataSource(
            @ActivityContext context: Context
        ): BleTemperatureSensorDataSource = BleTemperatureSensorDataSource(context)
    }
}
