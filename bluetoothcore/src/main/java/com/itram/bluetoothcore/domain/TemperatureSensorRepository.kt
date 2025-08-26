package com.itram.bluetoothcore.domain

import kotlinx.coroutines.flow.StateFlow

interface TemperatureSensorRepository {
    suspend fun connect(deviceAddress: String): Boolean
    suspend fun disconnect()
    suspend fun getTemperature(): Float?
    fun isConnected(): Boolean
    val connectionState: StateFlow<Boolean>
}
