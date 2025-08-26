package com.itram.bluetooth.ui

import android.util.Log
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemperatureViewModel @Inject constructor(
    dataSource: TemperatureSensorDataSource,
    scanBleDevicesUseCase: ScanBleDevicesUseCase
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

    private val _hasConnectedOnce = MutableStateFlow(false)
    val hasConnectedOnce: StateFlow<Boolean> = _hasConnectedOnce

    private val _snackbarError = MutableStateFlow<String?>(null)
    val snackbarError: StateFlow<String?> = _snackbarError

    private val _isConnecting = MutableStateFlow(false)
    val isConnecting: StateFlow<Boolean> = _isConnecting

    val connectionState: StateFlow<Boolean> = repository.connectionState

    val bleDevices: StateFlow<List<BleDevice>> = scanBleDevicesUseCase()
        .map { list ->
            list.filter { device ->
                device.address == "FF:FF:77:22:13:45"
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var lastDeviceAddress: String? = null

    init {
        // Log para depuración del flujo de conexión
        viewModelScope.launch {
            var lastConnected = false
            connectionState.collect { connected ->
                Log.d("BLE_VM", "connectionState: $connected, hasConnectedOnce: ${_hasConnectedOnce.value}")
                if (!connected && lastConnected) {
                    // Siempre que pase de conectado a desconectado, mostrar error
                    _snackbarError.value = "Se perdió la conexión con el sensor."
                    Log.d("SNACKBAR_DEBUG", "Desconexión detectada, snackbarError='Se perdió la conexión con el sensor.'")
                }
                lastConnected = connected
            }
        }
    }

    fun connect(deviceAddress: String) {
        viewModelScope.launch {
            _isConnecting.value = true
            android.util.Log.d("TEMP_DEBUG", "isConnecting = true (inicio connect)")
            val result = connectUseCase(deviceAddress)
            android.util.Log.i("BLE_UI", "Resultado de connectUseCase: $result")
            _isConnected.value = result
            _isConnecting.value = false
            android.util.Log.d("TEMP_DEBUG", "isConnecting = false (fin connect)")
            if (result) {
                lastDeviceAddress = deviceAddress
                _hasConnectedOnce.value = true
                _snackbarError.value = null
                android.util.Log.d("SNACKBAR_DEBUG", "connect: conexión exitosa, snackbarError=null")
            } else {
                // Mostrar error si falla la conexión inicial
                _snackbarError.value = "No se pudo conectar al sensor. Intenta de nuevo."
                android.util.Log.d("SNACKBAR_DEBUG", "connect: conexión fallida, snackbarError='No se pudo conectar al sensor. Intenta de nuevo.'")
            }
        }
    }

    fun retryConnection() {
        lastDeviceAddress?.let { address ->
            viewModelScope.launch {
                _isConnecting.value = true
                val result = connectUseCase(address)
                _isConnecting.value = false
                if (!result) {
                    _snackbarError.value = "No se pudo reconectar. Intenta de nuevo."
                    android.util.Log.d("SNACKBAR_DEBUG", "retryConnection: reconexión fallida, snackbarError='No se pudo reconectar. Intenta de nuevo.'")
                } else {
                    _snackbarError.value = null
                    android.util.Log.d("SNACKBAR_DEBUG", "retryConnection: reconexión exitosa, snackbarError=null")
                }
            }
        } ?: run {
            _snackbarError.value = "No hay dispositivo previo para reconectar."
            android.util.Log.d("SNACKBAR_DEBUG", "retryConnection: sin dispositivo previo, snackbarError='No hay dispositivo previo para reconectar.'")
        }
    }

    fun clearSnackbarError() {
        _snackbarError.value = null
        android.util.Log.d("SNACKBAR_DEBUG", "clearSnackbarError: snackbarError=null")
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
