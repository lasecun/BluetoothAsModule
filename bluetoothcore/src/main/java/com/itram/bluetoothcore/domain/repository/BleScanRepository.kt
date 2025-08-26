package com.itram.bluetoothcore.domain.repository

import com.itram.bluetoothcore.domain.model.BleDevice
import kotlinx.coroutines.flow.Flow

interface BleScanRepository {
    fun scanDevices(): Flow<List<BleDevice>>
}
