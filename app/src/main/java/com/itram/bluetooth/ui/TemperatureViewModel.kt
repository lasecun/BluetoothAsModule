package com.itram.bluetooth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itram.bluetoothcore.data.TemperatureSensorDataSource
import com.itram.bluetoothcore.data.TemperatureSensorRepositoryImpl
import com.itram.bluetoothcore.usecase.ConnectToSensorUseCase
import com.itram.bluetoothcore.usecase.DisconnectSensorUseCase
import com.itram.bluetoothcore.usecase.GetTemperatureUseCase
import com.itram.bluetoothcore.usecase.IsSensorConnectedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TemperatureViewModel @Inject constructor(
    dataSource: TemperatureSensorDataSource
) : ViewModel() {
    private val repository = TemperatureSensorRepositoryImpl(dataSource)
    private val connectUseCase = ConnectToSensorUseCase(repository)
    private val disconnectUseCase = DisconnectSensorUseCase(repository)
    private val getTemperatureUseCase = GetTemperatureUseCase(repository)
    private val isConnectedUseCase = IsSensorConnectedUseCase(repository)

    private val _temperature = MutableStateFlow<Float?>(null)
    val temperature: StateFlow<Float?> = _temperature

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    fun connect(deviceAddress: String) {
        viewModelScope.launch {
            val result = connectUseCase(deviceAddress)
            _isConnected.value = result
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            disconnectUseCase()
            _isConnected.value = false
        }
    }

    fun readTemperature() {
        viewModelScope.launch {
            _temperature.value = getTemperatureUseCase()
        }
    }

    fun checkConnection() {
        _isConnected.value = isConnectedUseCase()
    }
}
