package com.itram.bluetoothcore.data

import com.itram.bluetoothcore.domain.TemperatureSensorRepository

class TemperatureSensorRepositoryImpl(private val dataSource: TemperatureSensorDataSource) : TemperatureSensorRepository {
    override suspend fun connect(deviceAddress: String): Boolean = dataSource.connect(deviceAddress)
    override suspend fun disconnect() = dataSource.disconnect()
    override suspend fun getTemperature(): Float? = dataSource.readTemperature()
    override fun isConnected(): Boolean = dataSource.isConnected()
}

