package com.itram.bluetoothcore.data

interface TemperatureSensorDataSource {
    suspend fun connect(deviceAddress: String): Boolean
    suspend fun disconnect()
    suspend fun readTemperature(): Float?
    fun isConnected(): Boolean
}

