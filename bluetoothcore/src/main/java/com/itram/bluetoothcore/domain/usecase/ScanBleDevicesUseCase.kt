package com.itram.bluetoothcore.domain.usecase

import com.itram.bluetoothcore.domain.model.BleDevice
import com.itram.bluetoothcore.domain.repository.BleScanRepository
import kotlinx.coroutines.flow.Flow

class ScanBleDevicesUseCase(private val repository: BleScanRepository) {
    operator fun invoke(): Flow<List<BleDevice>> = repository.scanDevices()
}
