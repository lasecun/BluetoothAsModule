package com.itram.bluetooth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itram.bluetoothcore.data.TemperatureSensorDataSource
import com.itram.bluetoothcore.data.TemperatureSensorRepositoryImpl
import com.itram.bluetoothcore.domain.model.BleDevice
import com.itram.bluetoothcore.domain.usecase.ScanBleDevicesUseCase
import com.itram.bluetoothcore.usecase.ConnectToSensorUseCase
import com.itram.bluetoothcore.usecase.DisconnectSensorUseCase
import com.itram.bluetoothcore.usecase.GetTemperatureUseCase
import com.itram.bluetoothcore.usecase.IsSensorConnectedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemperatureViewModel @Inject constructor(
    dataSource: TemperatureSensorDataSource,
    private val scanBleDevicesUseCase: ScanBleDevicesUseCase
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

    val bleDevices: StateFlow<List<BleDevice>> = scanBleDevicesUseCase()
        .map { list ->
            list.filter { device ->
                device.address == "FF:FF:77:22:13:45"
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun connect(deviceAddress: String) {
        android.util.Log.i("BLE_UI", "Llamando a connect() con $deviceAddress")
        viewModelScope.launch {
            val result = connectUseCase(deviceAddress)
            android.util.Log.i("BLE_UI", "Resultado de connectUseCase: $result")
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
