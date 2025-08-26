package com.itram.bluetoothcore.data

import kotlinx.coroutines.flow.StateFlow

interface TemperatureSensorDataSource {
    suspend fun connect(deviceAddress: String): Boolean
    suspend fun disconnect()
    suspend fun readTemperature(): Float?
    fun isConnected(): Boolean
    val connectionState: StateFlow<Boolean>
}
