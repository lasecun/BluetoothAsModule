package com.itram.bluetoothcore.domain

interface TemperatureSensorRepository {
    suspend fun connect(deviceAddress: String): Boolean
    suspend fun disconnect()
    suspend fun getTemperature(): Float?
    fun isConnected(): Boolean
}

