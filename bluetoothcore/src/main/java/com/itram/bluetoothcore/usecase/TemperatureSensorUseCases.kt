package com.itram.bluetoothcore.usecase

import com.itram.bluetoothcore.domain.TemperatureSensorRepository

class GetTemperatureUseCase(private val repository: TemperatureSensorRepository) {
    suspend operator fun invoke(): Float? = repository.getTemperature()
}

class ConnectToSensorUseCase(private val repository: TemperatureSensorRepository) {
    suspend operator fun invoke(deviceAddress: String): Boolean = repository.connect(deviceAddress)
}

class DisconnectSensorUseCase(private val repository: TemperatureSensorRepository) {
    suspend operator fun invoke() = repository.disconnect()
}

class IsSensorConnectedUseCase(private val repository: TemperatureSensorRepository) {
    operator fun invoke(): Boolean = repository.isConnected()
}

