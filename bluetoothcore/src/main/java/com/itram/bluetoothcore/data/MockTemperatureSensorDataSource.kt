package com.itram.bluetoothcore.data

import kotlinx.coroutines.delay

class MockTemperatureSensorDataSource : TemperatureSensorDataSource {
    private var connected = false
    private var temperature = 25.0f

    override suspend fun connect(deviceAddress: String): Boolean {
        delay(500) // Simula retardo de conexión
        connected = true
        return connected
    }

    override suspend fun disconnect() {
        delay(200)
        connected = false
    }

    override suspend fun readTemperature(): Float? {
        delay(300)
        return if (connected) temperature else null
    }

    override fun isConnected(): Boolean = connected
}

